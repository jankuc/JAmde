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
import java.util.Scanner;

/**
 *
 * @author honza
 */
public class Table {
    private ArrayList<TableInput> tableInputs = new ArrayList<TableInput>();
    private ArrayList<TableOutput> tableOutputs = new ArrayList<TableOutput>(); // elements in the list are in the same order as the elements in the input

    
    public ArrayList<TableInput> getTableInputs() {
        return tableInputs;
    }

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
        double[] errProb = new double[2];
        ArrayList<double[]> errProbs = new ArrayList<double[]>();
        String line;

        int lineNumber = 1;

        while (sc.hasNext()) {
            if (sc.hasNext("#")) {
                if ((input.getSizeOfEstimator() != 0)) {
                    return input;
                }
                sc.next();
            } else if (lineNumber == 1) {
                line = sc.nextLine(); // sc is still at the end of #-line, so the first nextLine() loads just ""
                line = sc.nextLine(); // from now on sc is on the second line!
                Scanner scl = new Scanner(line);
                String nextString = scl.next(); // for the first line we use scl
                if (nextString.equals("file")) { // We either load a file, or we generate dataset
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
                } else { // We either generate dataset with mixture of distributions or distribution with errors of Order
                    input.setData(null);
                    sContaminated = nextString;
                    contaminatedPar1 = scl.nextDouble();
                    contaminatedPar2 = scl.nextDouble();
                    contaminatedPar3 = scl.nextDouble();
                    distBuilder.setDistribution(sContaminated, contaminatedPar1, contaminatedPar2, contaminatedPar3);
                    input.setContaminated(distBuilder.getDistribution());
                    nextString = scl.next();
                    if (nextString.equals("errorOfOrder") || nextString.contains("error") || nextString.contains("order") || nextString.contains("Error") || nextString.contains("Order")) { 
                        while (scl.hasNextDouble()) {
                            errProb = new double[2];
                            errProb[0] =  scl.nextDouble(); // type of error (coeficient which we use in multiplication)
                            errProb[1] = scl.nextDouble(); // probability
                            errProbs.add(errProb);
                        }
                        input.setOrderErrors(errProbs);
                    } else { // DataSet will be mixture of distributions
                        input.setOrderErrors(null);
                        sContaminating = nextString;
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

    public void count(int numOfThreads) throws FileNotFoundException, InterruptedException {
        System.out.println("Enumeration has begun");
        /*
         * TODO dat mistoa nazev souboru pro ulozeni do config souboru
         * TODO pri nacitani umisteni tabulky toto umisteni vytvori
         * TODO vytvorit funkci pro vykreslovani vzdalenostnich obrazku
         */
        int numOfTables = tableInputs.size();
        int countedTable = 0;
        for (TableInput input : tableInputs) { // cycle over all tables
            countedTable++;
            Long tableStartTime = System.currentTimeMillis();
            int numOfPars = 1;
            if (input.getParamsCounted().equals("both")) {
                numOfPars = 2;
            }
            TableOutput tableOutput = new TableOutput((ArrayList) input.getEstimators(), (ArrayList) input.getSizeOfSample(), numOfPars);
            EstimatorBuilder estimatorBuilderL1 = input.getEstimators().get(0);
            for (EstimatorBuilder estimatorBuilder : input.getEstimators()) { //cycle over all estimators in one table (lines of the table)
                Distribution[] estimatorArray = new Distribution[input.getSizeOfEstimator()]; // for every dsitribution in this array, we will find parameters which minimize the distance from this distribution to the current data.
                for (int i = 0; i < estimatorArray.length; i++) {
                    DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1(), input.getContaminated().getP2(), input.getContaminated().getP3());
                    estimatorArray[i] = dB.getDistribution();
                }
                for (int sizeOfSample : input.getSizeOfSample()) { // cycle over all the sizes of dataArray. (columns in the table) [20, 50, 100, 200, 500]
                    // Preparation for Threads:
                    int threadLoad = (int) (estimatorArray.length / numOfThreads); // Load which will be covered by each Thread
                    int leftover = estimatorArray.length - threadLoad * numOfThreads; // It will be distributed between Threads
                    int[] threadLoads = new int[numOfThreads];
                    for (int i = 0; i < threadLoads.length; i++) {
                        if (leftover > 0) {
                            threadLoads[i] = threadLoad + 1;
                            leftover--;
                        } else {
                            threadLoads[i] = threadLoad;
                        }
                    }
                    if (true) {
                        CountThread[] threadArray = new CountThread[numOfThreads]; // Array of Threads
                        Distribution[][] threadResult = new Distribution[numOfThreads][];
                        DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1(), input.getContaminated().getP2(), input.getContaminated().getP3());
                        for (int i = 0; i < threadArray.length; i++) { //0:numOfThreads-1
                            threadArray[i] = new CountThread(input, threadLoads[i], estimatorBuilder, sizeOfSample);
                            threadResult[i] = new Distribution[threadLoads[i]];
                        }
                        for (int i = 0; i < threadArray.length; i++) {
                            threadResult[i] = threadArray[i].startCount();
                        }
                        for (int i = 0; i < threadArray.length; i++) {
                            threadArray[i].join();
                            for (int j = 0; j < threadResult[i].length; j++) { // copying of result from thread to estimatorArray
                                int posun1 = i * threadLoads[i];
                                estimatorArray[posun1 + j] = threadResult[i][j];
                            }
                        }
                    } // End of enumeration
                    printToTableOutput(estimatorArray, input, tableOutput, estimatorBuilder, sizeOfSample);
                } // END for (int sizeOfSample : input.getSizeOfSample())
                System.out.println("Estimator " + estimatorBuilder.getType() + "(" + estimatorBuilder.getPar() + ") has ended.");
            }// END for (EstimatorBuilder estimatorBuilder : input.getEstimators())
            tableOutputs.add(tableOutput);
            Long tableEndTime = System.currentTimeMillis();
            Long tableTime = tableEndTime - tableStartTime;
            System.out.println("Table " + countedTable + "/" + numOfTables + " has ended. It took " + MathUtil.Long2time(tableTime) + ". It could take another " + MathUtil.Long2time((numOfTables - countedTable) * tableTime));
        }// END for (TableInput input : tableInputs)
        System.out.println("Enumeration has ended.");
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
            printClassicHeadTable(file, input);
            int index = tableInputs.indexOf(input);
            TableOutput output = tableOutputs.get(index);
            for (EstimatorBuilder estBuilder : input.getEstimators()) {
                printClassicLine(file, input, output, estBuilder);
            }
            printClassicEndTable(file, input);
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
        FileWriter w = new FileWriter(file, true); // so it appends
        w.write("\\end{document}\n");
        w.close();
    }

    public void printClassicHeadTable(File file, TableInput input) throws IOException {

        Integer[] sizeOfSample = input.getSizeOfSample().toArray(new Integer[0]);
        FileWriter w = new FileWriter(file, true); // so it appends
        w.write("\\begin{table}[ht] \\footnotesize \n");
        w.write("\\begin{center} \n");
        w.write("\\begin{tabular}{|c|");
        for (int i : sizeOfSample) {
            w.write("ccc|");
        }
        w.write("} \n");
        w.write("\\hline \n"); // the upper border line
        w.write("$\\alpha\\backslash n$ &&  $" + sizeOfSample[0] + "$"); // line with sizes of sample
        for (int i = 1; i < sizeOfSample.length; i++) { // I really want to go through items {1 : end} not {0:end}
            w.write(" &&&  $" + sizeOfSample[i] + "$");
        }
        w.write(" & \\\\ \n"); // ending of line with sizes of sample
        w.write("\\hline \n"); // borderline
        if (input.getParamsCounted().equals("first") || input.getParamsCounted().equals("both")) { // line in the head of the parameter mu
            for (int i = 0; i < sizeOfSample.length; i++) {
                w.write("& $m(\\mu)$ & $s(\\mu)$ & $eref(\\mu)$ ");
            }
            w.write("\\\\ \n");
        }
        if (input.getParamsCounted().equals("second") || input.getParamsCounted().equals("both")) { // line in the head of the parameter sigma
            for (int i = 0; i < sizeOfSample.length; i++) {
                w.write("& $m(\\sigma)$ & $s(\\sigma)$ & $eref(\\sigma)$ ");
            }
            w.write("\\\\ \n");
        }
        w.write("\\hline \n"); // border line below the head of the table.
        w.close();
    }

    public void printClassicEndTable(File file, TableInput input) throws IOException {
        FileWriter ww = new FileWriter(file, true); // so it appends
        PrintWriter w = new PrintWriter(ww);
        w.write("\\end{tabular}\n");
        ArrayList<EstimatorBuilder> eBs = input.getEstimators();
        String sContaminated = String.format("\\mathrm{%c}(%.0f,%.0f)", input.getContaminated().toString().charAt(0), input.getContaminated().getP1(), input.getContaminated().getP2());
        w.write("\\caption{" + eBs.get(1).getType() + ": $p = " + sContaminated + "$, data: ");        
        if (input.getContaminating() == null) {
            if (input.getData() == null) {
                ArrayList orderErrors = input.getOrderErrors();
                double prob = 1;
                double[] probs = new double[orderErrors.size()];
                double[] orders = new double[orderErrors.size()];
                String[] distrs = new String[orderErrors.size()];
                for (int i = 0; i < orderErrors.size(); i++) {
                    orders[i] = ((double[])orderErrors.get(i))[0];
                    probs[i] = ((double[])orderErrors.get(i))[1];
                    prob -= probs[i]; 
                    distrs[i] = String.format("%.1f\\mathrm{%c}_{%.1fx}(%.0f,%.0f)",probs[i],input.getContaminated().toString().charAt(0),orders[i], input.getContaminated().getP1(), input.getContaminated().getP2());
                }
                w.write("$" + prob + sContaminated);
                for (int i = 0; i < distrs.length; i++) {
                    w.write(" + " + distrs[i]);
                }
                w.write("$, $K = " + input.getSizeOfEstimator() + "$} \n");    
            } else {
                w.write(" Data na4ten8 ze souboru. \n");
            }
        } else {
            String sContaminating = String.format("mathrm{%c}(%.0f,%.0f)", input.getContaminating().toString().charAt(0), input.getContaminating().getP1(), input.getContaminating().getP2());
            w.write("$(1-\\varepsilon)" + sContaminated + " + \\varepsilon " + sContaminating + "$, $\\varepsilon =  " + input.getContamination() + "$, $K = " + input.getSizeOfEstimator() + "$} \n");
        }
        w.write("\\end{center}\n");
        w.write("\\end{table}\n");
        w.close();
    }

    private void printClassicLine(File file, TableInput input, TableOutput output, EstimatorBuilder estimator) throws IOException {
        FileWriter ww = new FileWriter(file, true); // so it appends
        PrintWriter w = new PrintWriter(ww);
        if (input.getParamsCounted().equals("first") || input.getParamsCounted().equals("both")) {
            w.write("$" + estimator.getPar() + "$");
            for (int sizeOfSample : input.getSizeOfSample()) {
                w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $", output.getMeanValue(estimator, sizeOfSample, 1), output.getDeviation(estimator, sizeOfSample, 1), output.getEfficiency(estimator, sizeOfSample, 1));
            }
            w.write("\\\\ \n");
        }
        if (input.getParamsCounted().equals("second") || input.getParamsCounted().equals("both")) {
            if (input.getParamsCounted().equals("second")) {
                w.write("$" + estimator.getPar() + "$");
            }
            for (int sizeOfSample : input.getSizeOfSample()) {
                w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $", output.getMeanValue(estimator, sizeOfSample, 2), output.getDeviation(estimator, sizeOfSample, 2), output.getEfficiency(estimator, sizeOfSample, 2));
            }
            w.write("\\\\ \n");
        }
        w.write("\\hline \n");
        w.close();
    }

    private TableOutput printToTableOutput(Distribution[] estimatorArray, TableInput input, TableOutput tableOutput, EstimatorBuilder estimatorBuilder, int sizeOfSample) {
        int numOfPars = 1;
        if (input.getParamsCounted().equals("both")) {
            numOfPars = 2;
        }
        EstimatorBuilder estimatorBuilderL1 = input.getEstimators().get(0);

        if (input.getParamsCounted().equals("both") || input.getParamsCounted().equals("first")) {
            double[] firstPar = new double[input.getSizeOfEstimator()];
            for (int i = 0; i < firstPar.length; i++) {
                firstPar[i] = estimatorArray[i].getP1();
            }
            double expVal1 = MathUtil.getExpVal(firstPar);
            double standVar1 = MathUtil.getStandVar(expVal1, firstPar);
            double standDev1 = Math.sqrt(standVar1);

            tableOutput.setMeanValue(estimatorBuilder, sizeOfSample, 1, expVal1);
            tableOutput.setDeviation(estimatorBuilder, sizeOfSample, 1, standDev1);
            double eref1 = Math.pow(tableOutput.getDeviation(estimatorBuilderL1, sizeOfSample, 1), 2) / standVar1; // radim pocital  eef =  varX/varL1 (bez odmocniny)
            //eref1 = Math.sqrt(eref1);
            tableOutput.setEfficiency(estimatorBuilder, sizeOfSample, 1, eref1);
        }
        if (input.getParamsCounted().equals("both") || input.getParamsCounted().equals("second")) {
            double[] secondPar = new double[input.getSizeOfEstimator()];
            for (int i = 0; i < secondPar.length; i++) {
                secondPar[i] = estimatorArray[i].getP2();
            }
            double expVal2 = MathUtil.getExpVal(secondPar);
            double standVar2 = MathUtil.getStandVar(expVal2, secondPar);
            double standDev2 = Math.sqrt(standVar2);
            tableOutput.setMeanValue(estimatorBuilder, sizeOfSample, numOfPars, expVal2);
            tableOutput.setDeviation(estimatorBuilder, sizeOfSample, numOfPars, standDev2);
            double eref2 = Math.pow(tableOutput.getDeviation(estimatorBuilderL1, sizeOfSample, numOfPars), 2) / standVar2;
            //eref2 = Math.sqrt(eref2);
            tableOutput.setEfficiency(estimatorBuilder, sizeOfSample, numOfPars, eref2);
        }
        return tableOutput;
    }

    public void printDistanceMatrix(TableInput input, Estimator estimator, int sizeOfSample) {
        double[] dataArray = createData(input, sizeOfSample);
        PrintWriter w;
        try {
            w = new PrintWriter("./distances");
            double mu;
            double sigma2;
            double dist;
            Distribution d = new NormalDistribution(0, 1);
            double N = 200;
            double delkaIntervalu = 1;
            for (int j = 0; j < N; j++) {
                mu = -0.5 * delkaIntervalu + j * delkaIntervalu / N;
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
        } catch (FileNotFoundException ex) {
        }
    }
    
    public void printDistanceMatrix(TableInput input, EstimatorBuilder eB, int sizeOfSample) {
        Estimator e = eB.getEstimator();
        printDistanceMatrix(input, e, sizeOfSample);
    }
    
    public void printDistanceMatrix(TableInput input, String estType, double estPar, int sizeOfSample) {
        EstimatorBuilder eB = new EstimatorBuilder(estType, estPar);
        printDistanceMatrix(input, eB, sizeOfSample);
    }
    
    public static double[] createData(TableInput input, int sizeOfSample) { // making dataSet or loading it from file
        double[] dataArray = new double[sizeOfSample];
        Distribution contaminated = input.getContaminated();
        Distribution contaminating = input.getContaminating();
        double contamination = input.getContamination();

        if (contaminating == null) { // if true then it's not mixture of distributions
            if (input.getOrderErrors() == null) { // if true then data was loaded from file
                System.arraycopy(input.getData(), 0, dataArray, 0, dataArray.length);
            } else { // data will be created as a distribution with errors in order\
                ArrayList Errors = input.getOrderErrors();
                double[] orderError = new double[Errors.size() + 1];
                double[] tmp =  new double[2];
                double[] cumulativeProbability = new double[Errors.size() + 1];
                for (int i = 0; i < Errors.size(); i++) {
                    tmp = (double[]) Errors.get(i);
                    orderError[i] = tmp[0]; // type of error (coeficient which we use in multiplication)
                    if (i==0) {
                        cumulativeProbability[i] = tmp[1];
                    } else {
                        cumulativeProbability[i] = tmp[1] + cumulativeProbability[i-1];
                    }
                }
                cumulativeProbability[cumulativeProbability.length-1] = 1; // probability of the last element is 1 
                orderError[orderError.length-1] = 1; // no error
                
                Distribution d = new UniformDistribution(0,1);
                double prob = 0;
                int i = 0;
                for (int j = 0; j < dataArray.length; j++) {
                    prob = d.getRealization();
                    while (prob > cumulativeProbability[i]) {
                        i++;
                    }
                    dataArray[j] = contaminated.getRealization() * orderError[i];
                    i = 0;
                }
            }
        } else { // data will be created as mixture of two distributions
            Distribution alternative = new AlternativeDistribution(contamination);
            for (int j = 0; j < dataArray.length; j++) {
                if (alternative.getRealization() < 0.5) { // if alternative = 0.0 <=> we do not contaminate (we do not use equality because of rounding errors)
                    dataArray[j] = contaminated.getRealization();
                } else {
                    dataArray[j] = contaminating.getRealization();
                }
            }
        }
        return dataArray;
    }
}
