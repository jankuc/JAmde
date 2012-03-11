/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.MathUtil;
import jamde.distribution.*;
import jamde.estimator.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 *
 * @author honza
 */
public class Table {
    private ArrayList<TableInput> tableInputs = new ArrayList<TableInput>();;
    private ArrayList<TableOutput> tableOutputs = new ArrayList<TableOutput>(); // elements in the list are in the same order as the elements in the input

    public ArrayList<TableInput> getTableInputs() {
        return tableInputs;
    }
    
//    public void setTableInputs(ArrayList<TableInput> tableInputs) {
//        this.tableInputs = tableInputs;
//    }
    
    public void addTableInput(TableInput tableInput) {
        this.tableInputs.add(tableInput);
    }

    public ArrayList<TableOutput> getTableOutputs() {
        return tableOutputs;
    }

    public void setTableOutputs(ArrayList<TableOutput> tableOutputs) {
        this.tableOutputs = tableOutputs;
    }
    
    public void addTableOutput(TableOutput tableOutput) {
        this.tableOutputs.add(tableOutput);
    }
    
    public void loadInputsFromFile(File confFile) throws Exception {
        System.out.println("You have opened configuration file " + confFile.toString() + "\n");
        
        ArrayList<TableInput> inputs = new ArrayList<TableInput>();
        TableInput input = new TableInput();
        
        Scanner sc = new Scanner(confFile);
        
        while (sc.hasNext()) {
            if (sc.hasNext("#")) {
                sc.next();
                input = loadInputFromFile(sc);
                if (input != null) {
                    inputs.add(input);
                }
            }
        }       
        tableInputs = inputs;
    }
    
    private TableInput loadInputFromFile(Scanner sc) throws Exception {
        TableInput input = new TableInput();
        String sContaminated, sContaminating, estType = "";
        double contaminatedPar1 = 0, contaminatedPar2 = 1, contaminatedPar3 = 0, contaminatingPar1 = 0, contaminatingPar2 = 1, contaminatingPar3 = 1, estPar = 0;
        ArrayList<Integer> sizeOfSample = new ArrayList<Integer>();
        
        ArrayList<EstimatorBuilder> estimators = new ArrayList<EstimatorBuilder>();
        EstimatorBuilder e = new EstimatorBuilder(estType, estPar);
        input.setSizeOfEstimator(0);
        DistributionBuilder distBuilder = new DistributionBuilder();
        Double[] errProb = new Double[2];
        ArrayList<Double[]> errProbs = new ArrayList<Double[]>();
        String line;
        
        int lineNumber = 1;
        
        while (sc.hasNext()) {    
            if (sc.hasNext("#")) {
                if ((input.getSizeOfEstimator()!=0)) {
                    return input;
                }
                sc.next();
            } else if (lineNumber == 1) {
                line = sc.nextLine(); // sc is still at the end of #-line, so the first nextLine() loads just ""
                line = sc.nextLine(); // from now on sc is on the second line!
                Scanner scl = new Scanner(line);
                String tmp = scl.next(); // for the first line we use scl
                if (tmp.equals("file")) { // We either load a file, or we generate dataset 
                    String filePath = scl.next();
                    sContaminated = scl.next();
                    contaminatedPar1 = scl.nextDouble();
                    contaminatedPar2 = scl.nextDouble();
                    contaminatedPar3 = scl.nextDouble();
                    distBuilder.setDistribution(sContaminated, contaminatedPar1, contaminatedPar2, contaminatedPar3);
                    input.setContaminated(distBuilder.getDistribution());
                    input.setContaminating(null);
                    input.setOrderErrors(null);
                    /*
                     * TODO neco jako input.setData(load(filePath));
                     */
                    throw new UnsupportedOperationException("Not supported yet.");
                } else {
                    input.setData(null);
                    sContaminated = tmp;
                    contaminatedPar1 = scl.nextDouble();
                    contaminatedPar2 = scl.nextDouble();
                    contaminatedPar3 = scl.nextDouble();
                    distBuilder.setDistribution(sContaminated, contaminatedPar1, contaminatedPar2, contaminatedPar3);
                    input.setContaminated(distBuilder.getDistribution());
                    tmp = scl.next();
                    if (tmp.equals("errorOfOrder")) { // We either generate dataset with mixture of distributions or distribution with errors
                        while (scl.hasNextDouble()) {
                            errProb[0] = scl.nextDouble();
                            errProb[1] = scl.nextDouble();
                            errProbs.add(errProb);
                        }
                        input.setOrderErrors(errProbs);
                    } else {
                        input.setOrderErrors(null);
                        sContaminating = tmp;
                        contaminatingPar1 = scl.nextDouble();
                        contaminatingPar2 = scl.nextDouble();
                        contaminatingPar3 = scl.nextDouble();
                        distBuilder.setDistribution(sContaminating, contaminatingPar1, contaminatingPar2, contaminatingPar3);
                        input.setContaminating(distBuilder.getDistribution());
                        input.setContamination(scl.nextDouble());
                    }
                }
                lineNumber++;
            } else if (lineNumber == 2) {
                input.setParamsCounted(sc.next());
                input.setSizeOfEstimator(sc.nextInt());
                lineNumber++;
            } else if (lineNumber == 3) {
                while (sc.hasNextInt()) {
                    sizeOfSample.add(sc.nextInt());
                }
                input.setSizeOfSample(sizeOfSample);
                lineNumber++;
            } else if (lineNumber == 4) {
                estType = sc.next();
                estPar = sc.nextDouble();
                e = new EstimatorBuilder(estType, estPar);
                estimators.add(e);
                input.setEstimators(estimators);
            }
        }//END while(sc.hasNext()) 
        return null;
    }
    
    public void count() throws FileNotFoundException {  
        for (TableInput input : tableInputs) { // cycle over all tables
            int numOfPars = 1;
            if (input.getParamsCounted().equals("both")) {
                numOfPars = 2;
            }
            TableOutput tableOutput = new TableOutput((ArrayList) input.getEstimators(),(ArrayList) input.getSizeOfSample() , numOfPars);
            EstimatorBuilder estimatorBuilderL1 = input.getEstimators().get(0); 
            for (EstimatorBuilder estimatorBuilder : input.getEstimators()) { //cycle over all estimators in one table (lines of the table)
                
                Estimator estimator = estimatorBuilder.getEstimator();

                Distribution contaminated = input.getContaminated();
                Distribution[] estimatorArray = new Distribution[input.getSizeOfEstimator()]; // for every dsitribution in this array, we will find parameters which minimize the distance from this distribution to the current data.
                //DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(),input.getContaminated().getP1(),  input.getContaminated().getP2(),input.getContaminated().getP3());
                //Arrays.fill(estimatorArray, dB.getDistribution()); // if we don't want to find both parameters of the distribution, the one which we aren't counting is correctly in its place

                for (int sizeOfSample : input.getSizeOfSample()) { // cycle over all the sizes of dataArray. (columns in the table)
                    for (int i = 0; i < estimatorArray.length; i++) { // cycle counting all the best distributions in estimator Array
                        // DATA creation
                        double[] dataArray = new double[sizeOfSample];
                        if (input.getContaminating() == null) { // if true then it's not mixture of distributions
                            if (input.getOrderErrors() == null) { // if true then data was loaded from file
                                System.arraycopy(input.getData(), 0, dataArray, 0, dataArray.length);
                            } else { // data will be created as a distribution with errors in order
                                Distribution alt = new AlternativeDistribution(input.getContamination());
                                /*
                                 * TODO vytvoreni dat s ustrely o rad
                                 */
                            }
                        } else { // data will be created as mixture of two distributions
                            Distribution contaminating = input.getContaminating();
                            Distribution alternative = new AlternativeDistribution(input.getContamination());
                            for (int j = 0; j < dataArray.length; j++) {
                                if (alternative.getRealization() < 0.5) { // if alternative = 0.0 <=> we do not contaminate (we do not use equality because of rounding errors)
                                    dataArray[j] = contaminated.getRealization();
                                } else {
                                    dataArray[j] = contaminating.getRealization();
                                }
                            }
                        } // Here the dataArray is completely filled with data
                        //printDataArray:
//                        for(double k : dataArray){
//                            System.out.println(k);
//                        }
//                        
                        if (false) {
                            PrintWriter w = new PrintWriter("./distances");
                            double mu;
                            double sigma2;
                            double dist;
                            Distribution d = new NormalDistribution(0, 1);
                            double N = 200;
                            double delkaIntervalu = 1;
                            for (int j = 0; j < N; j++) {
                                mu = -0.5*delkaIntervalu + j*delkaIntervalu/ N;
                                for (int k = 0; k < N; k++) {
                                    sigma2 = 0.0001 + k * 2 * delkaIntervalu / N;
                                    d.setParameters(mu, sigma2, 0);
                                    dist = estimator.countDistance(d, dataArray);
                                    w.format(" %.6f ", dist);
                                }
                                w.format(" \n ");
                            }
                            w.format(" \n ");
                            w.close();
                            System.out.println("Vypsani Distance souboru skonceno.");
                            return;
                        }
                        // this is where the minimalization begins     
                        if (input.getParamsCounted().equals("both")) {
                            DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1() ,  input.getContaminated().getP2(), input.getContaminated().getP3());
                            estimatorArray[i] = estimator.minimalize(dB.getDistribution(), dataArray);
                        } else if (input.getParamsCounted().equals("first")) {
                            /*
                             * TODO dodelat minimalizaci pro jeden parametr
                             * v tuto chvili vraci jen contaminated distribution
                             */
                            estimatorArray[i] = estimator.minimalizeFirstPar(estimatorArray[i], dataArray);
                        } else {
                            estimatorArray[i] = estimator.minimalizeSeconPar(estimatorArray[i], dataArray);
                        }
                    } // END for (i = 0; estimatorArray[i] : estimatorArray)
                    if (input.getParamsCounted().equals("both")) {
                        double[] firstPar = new double[input.getSizeOfEstimator()];
                        double[] secondPar = new double[input.getSizeOfEstimator()];
                        for (int i = 0; i < firstPar.length; i++) {
                            firstPar[i] = estimatorArray[i].getP1();
                            secondPar[i] = Math.sqrt(estimatorArray[i].getP2());
                        }
                        double expVal1 = MathUtil.getExpVal(firstPar);
                        double standVar1 = MathUtil.getStandVar(expVal1, firstPar);
                        
                        double standDev1 = Math.sqrt(standVar1);
                        
                        tableOutput.setMeanValue(estimatorBuilder, sizeOfSample, 1, expVal1);
                        tableOutput.setDeviation(estimatorBuilder, sizeOfSample, 1, standDev1);
                        double eref1 = Math.pow(tableOutput.getDeviation(estimatorBuilderL1, sizeOfSample, 1),2)  / standVar1 ;
                        tableOutput.setEfficiency(estimatorBuilder, sizeOfSample, 1, eref1);
                        
                        double expVal2 = MathUtil.getExpVal(secondPar);
                        double standVar2 = MathUtil.getStandVar(expVal2, secondPar);
                       
                        double standDev2 = Math.sqrt(standVar2);
                        
                        tableOutput.setMeanValue(estimatorBuilder, sizeOfSample, 2, expVal2);
                        tableOutput.setDeviation(estimatorBuilder, sizeOfSample, 2, standDev2);
                        double eref2 = Math.pow(tableOutput.getDeviation(estimatorBuilderL1, sizeOfSample, 2),2)  / standVar2 ;
                        tableOutput.setEfficiency(estimatorBuilder, sizeOfSample, 2, eref2);
                                          
                        break;
                    } else if (input.getParamsCounted().equals("first")) {
                        double[] firstPar = new double[input.getSizeOfEstimator()];
                        for (int i = 0; i < firstPar.length; i++) {
                            firstPar[i] = estimatorArray[i].getP1();
                        }
                        double expVal1 = MathUtil.getExpVal(firstPar);
                        double standVar1 = MathUtil.getStandVar(expVal1, firstPar);
                        
                        double standDev1 = Math.sqrt(standVar1);
                        
                        tableOutput.setMeanValue(estimatorBuilder, sizeOfSample, 1, expVal1);
                        tableOutput.setDeviation(estimatorBuilder, sizeOfSample, 1, standDev1);
                        double eref1 = Math.pow(tableOutput.getDeviation(estimatorBuilderL1, sizeOfSample, 1),2)  / standVar1 ;
                        tableOutput.setEfficiency(estimatorBuilder, sizeOfSample, 1, eref1);
                        
                        
                        break;
                    } else {
                        double[] secondPar = new double[input.getSizeOfEstimator()];
                        for (int i = 0; i < secondPar.length; i++) {
                            secondPar[i] = estimatorArray[i].getP2();
                        }
                        double expVal2 = MathUtil.getExpVal(secondPar);
                        double standVar2 = MathUtil.getStandVar(expVal2, secondPar);
                        
                        double standDev2 = Math.sqrt(standVar2);
                        
                        tableOutput.setMeanValue(estimatorBuilder, sizeOfSample, 2, expVal2);
                        tableOutput.setDeviation(estimatorBuilder, sizeOfSample, 2, standDev2);
                        double eref2 = Math.pow(tableOutput.getDeviation(estimatorBuilderL1, sizeOfSample, 2),2)  / standVar2 ;
                        tableOutput.setEfficiency(estimatorBuilder, sizeOfSample, 2, eref2);
                    }       
                } // END for (int sizeOfSample : input.getSizeOfSample())
            }// END for (EstimatorBuilder estimatorBuilder : input.getEstimators())
            
            tableOutputs.add(tableOutput);
        }// END for (TableInput input : tableInputs)
    } // END count()
    
    public void printClassic(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            /*
             * TODO dodelat dotaz na prepsani stareho souboru/vytvoreni noveho jmena souboru 
             */ 
        } // at the end of this condition we have desired fileName (it was changed, if there were collisions)
        file.createNewFile();
        printClassicHeadTex(file);
        for (TableInput input : tableInputs) {
            printClassicHeadTable(file,input);
            int index = tableInputs.indexOf(input);
            TableOutput output = tableOutputs.get(index);
            for (EstimatorBuilder estBuilder : input.getEstimators() ) {
                printClassicLine(file, input, output, estBuilder);
            }
            printClassicEndTable(file,input);
        }
        printClassicEndTex(file);
    } // END printClassic(String fileName)
    
    public void printClassicHeadTex(File file) throws IOException {
        FileWriter w = new FileWriter(file);
        w.write("\\documentclass[11pt]{article}\n");
	w.write("\\usepackage[utf8]{inputenc}\n");
	w.write("\\usepackage[czech]{babel}\n");
	w.write("\\usepackage[landscape]{geometry}\n");
	w.write("\\usepackage{pdflscape}\n");
	w.write("\\textwidth 210mm \\textheight 275mm \\oddsidemargin -5mm\n");
	w.write("\\evensidemargin 3mm \\topmargin -25mm\n");
	w.write("\\newlength{\\defbaselineskip}\n");
	w.write("\\setlength{\\defbaselineskip}{\\baselineskip}\n");
	w.write("\\newcommand{\\setlinespacing}[1]\n");
	w.write("   {\\setlength{\\baselineskip}{#1 \\defbaselineskip}}\n");
	w.write("\\pagestyle{empty}\n");
	w.write("\\begin{document}\n");
        w.close();
    }
    
    public void printClassicEndTex(File file) throws IOException {
        FileWriter w = new FileWriter(file,true); // so it appends
	w.write("\\end{document}\n");
        w.close();
    }
    
    public void printClassicHeadTable(File file, TableInput input) throws IOException {
        
        Integer[] sizeOfSample =  input.getSizeOfSample().toArray(new Integer[0]);
        FileWriter w = new FileWriter(file,true); // so it appends
	w.write("\\begin{table}[ht] \\footnotesize \n");
        w.write("\\begin{center} \n");
        w.write("\\begin{tabular}{|c|");
        for (int i : sizeOfSample){
            w.write("ccc|");
        }
        w.write("} \n");
        w.write("\\hline \n"); // the upper border line
        w.write("$\\alpha\\backslash n$ &&  $"+ sizeOfSample[0] +"$"); // line with sizes of sample
        for (int i = 1; i < sizeOfSample.length; i ++) { // I really want to go through items {1 : end} not {0:end}
            w.write(" &&&  $" + sizeOfSample[i] + "$");
        }
        w.write(" & \\\\ \n"); // ending of line with sizes of sample
        w.write("\\hline \n"); // borderline
        if (input.getParamsCounted().equals("first") || input.getParamsCounted().equals("both")) { // line in the head of the parameter mu
            for(int i = 0; i < sizeOfSample.length; i++) {
                w.write("& $m(\\mu)$ & $s(\\mu)$ & $eef(\\mu)$ ");
            }
            w.write("\\\\ \n");
        }
        if (input.getParamsCounted().equals("second") || input.getParamsCounted().equals("both")) { // line in the head of the parameter sigma
            for(int i = 0; i < sizeOfSample.length; i++) {
                w.write("& $m(\\sigma)$ & $s(\\sigma)$ & $eef(\\sigma)$ ");
            }
            w.write("\\\\ \n");
        }
        w.write("\\hline \n"); // border line below the head of the table.
        w.close();
    }
    
    public void printClassicEndTable(File file, TableInput input) throws IOException {
        FileWriter ww = new FileWriter(file,true); // so it appends
        PrintWriter w = new PrintWriter(ww);
	w.write("\\end{tabular}\n");
        ArrayList<EstimatorBuilder> eBs = input.getEstimators();
        
        String sContaminated = String.format("%c(%.0f,%.0f)", input.getContaminated().toString().charAt(0), input.getContaminated().getP1(), input.getContaminated().getP2());
        String sContaminating = String.format("%c(%.0f,%.0f)", input.getContaminating().toString().charAt(0), input.getContaminating().getP1(), input.getContaminating().getP2());
        w.write("\\caption{" + eBs.get(1).getType() + ": $p = " + sContaminated + "$, data: $(1-\\varepsilon)" + sContaminated + " + \\varepsilon " + sContaminating + "$, $\\varepsilon =  " + input.getContamination() + "$, $K = " + input.getSizeOfEstimator() + "$} \n");
        w.write("\\end{center}\n");
        w.write("\\end{table}\n");
        w.close();
    }

    private void printClassicLine(File file, TableInput input,TableOutput output, EstimatorBuilder estimator) throws IOException {
        FileWriter ww = new FileWriter(file,true); // so it appends
        PrintWriter w = new PrintWriter(ww);
	if(input.getParamsCounted().equals("first") || input.getParamsCounted().equals("both") ){
            w.write("$" + estimator.getPar() + "$");
            for(int sizeOfSample : input.getSizeOfSample()){
                w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $",output.getMeanValue(estimator, sizeOfSample, 1), output.getDeviation(estimator, sizeOfSample, 1), output.getEfficiency(estimator, sizeOfSample, 1));
            }
            w.write("\\\\ \n");
        }
        if(input.getParamsCounted().equals("second") || input.getParamsCounted().equals("both") ){
            if (input.getParamsCounted().equals("second")) {
                w.write("$" + estimator.getPar() + "$");
            } 
            for(int sizeOfSample : input.getSizeOfSample()){
                w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $",output.getMeanValue(estimator, sizeOfSample, 2), output.getDeviation(estimator, sizeOfSample, 2), output.getEfficiency(estimator, sizeOfSample, 2));
            }
            w.write("\\\\ \n");
        }
        w.write("\\hline \n");
        //  $0$ &  $-0.001$ &  $0.224$ &  $1.000$ &  $0.009$ &  $0.145$ &  $1.000$ &  $-0.002$ &  $0.101$ &  $1.000$ &  $-0.004$ &  $0.069$ &  $1.000$ &  $-0.001$ &  $0.044$ &  $1.000$ \\
        w.close();
    }
}
