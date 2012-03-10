/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.MathUtil;
import jamde.distribution.*;
import jamde.estimator.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 *
 * @author honza
 */
public class Table {
    private Collection<TableInput> tableInputs = new ArrayList<TableInput>();;
    private Collection<TableOutput> tableOutputs = new ArrayList<TableOutput>();

    public Collection<TableInput> getTableInputs() {
        return tableInputs;
    }
    
    public void setTableInputs(Collection<TableInput> tableInputs) {
        this.tableInputs = tableInputs;
    }
    
    public void addTableInput(TableInput tableInput) {
        this.tableInputs.add(tableInput);
    }

    public Collection<TableOutput> getTableOutputs() {
        return tableOutputs;
    }

    public void setTableOutputs(Collection<TableOutput> tableOutputs) {
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
    
    public void count() {  
        for (TableInput input : tableInputs) { // cycle over all tables
            int numOfPars = 1;
            if (input.getParamsCounted().equals("both")) {
                numOfPars = 2;
            }
            TableOutput tableOutput = new TableOutput((ArrayList) input.getEstimators(),(ArrayList) input.getSizeOfSample() , numOfPars);
                   
            for (EstimatorBuilder estimatorBuilder : input.getEstimators()) { //cycle over all estimators in one table (lines of the table)
                Estimator estimator = estimatorBuilder.getEstimator();

                Distribution contaminated = input.getContaminated();
                Distribution[] estimatorArray = new Distribution[input.getSizeOfEstimator()]; // for every dsitribution in this array, we will find parameters which minimize the distance from this distribution to the current data.
                Arrays.fill(estimatorArray, contaminated); // if we don't want to find both parameters of the distribution, the one which we aren't counting is correctly in its place

                for (int sizeOfSample : input.getSizeOfSample()) { // cycle over all the sizes of dataArray. (columns in the table)
                    double[] dataArray = new double[sizeOfSample];
                    if (input.getContaminating()==null){ // if true then it's not mixture of distributions
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
                        for (int i = 0; i < dataArray.length; i++) {
                            if (alternative.getRealization() < 0.5) { // if alternative = 0.0 <=> we do not contaminate (we do not use equality because of rounding errors)
                                dataArray[i] = contaminated.getRealization();
                            } else {
                                dataArray[i] = contaminating.getRealization();
                            }
                        }
                    }
                    
                    for (Distribution closestDistribution : estimatorArray) { // cycle counting all the best distributions in estimator Array
                        if (input.getParamsCounted().equals("both")){
                            closestDistribution = estimator.minimalize(closestDistribution, dataArray);
                        } else if (input.getParamsCounted().equals("first")) {
                            closestDistribution = estimator.minimalizeFirstPar(closestDistribution, dataArray);
                        } else {
                            closestDistribution = estimator.minimalizeSeconPar(closestDistribution, dataArray);
                        }
                    } // END for (Distribution closestDistribution : estimatorArray)
                    if (input.getParamsCounted().equals("both")) {
                        double[] firstPar = new double[input.getSizeOfEstimator()];
                        double[] secondPar = new double[input.getSizeOfEstimator()];
                        for (int i = 0; i < firstPar.length; i++) {
                            firstPar[i] = estimatorArray[i].getP1();
                            secondPar[i] = estimatorArray[i].getP2();
                        }
                        double expVal1 = MathUtil.getExpVal(firstPar);
                        double standVar1 = MathUtil.getStandVar(expVal1, firstPar);
                        double eref1 = Math.pow(tableOutput.getDeviation(estimator, sizeOfSample, 1),2)  / standVar1 ;
                        double standDev1 = Math.sqrt(standVar1);
                        
                        tableOutput.setMeanValue(estimator, sizeOfSample, 1, expVal1);
                        tableOutput.setDeviation(estimator, sizeOfSample, 1, standDev1);
                        tableOutput.setEfficiency(estimator, sizeOfSample, 1, eref1);
                        
                        double expVal2 = MathUtil.getExpVal(secondPar);
                        double standVar2 = MathUtil.getStandVar(expVal2, secondPar);
                        double eref2 = Math.pow(tableOutput.getDeviation(estimator, sizeOfSample, 2),2)  / standVar2 ;
                        double standDev2 = Math.sqrt(standVar2);
                        
                        tableOutput.setMeanValue(estimator, sizeOfSample, 2, expVal2);
                        tableOutput.setDeviation(estimator, sizeOfSample, 2, standDev2);
                        tableOutput.setEfficiency(estimator, sizeOfSample, 2, eref2);
                        
                        
                        break;
                    } else if (input.getParamsCounted().equals("first")) {
                        double[] firstPar = new double[input.getSizeOfEstimator()];
                        for (int i = 0; i < firstPar.length; i++) {
                            firstPar[i] = estimatorArray[i].getP1();
                        }
                        double expVal1 = MathUtil.getExpVal(firstPar);
                        double standVar1 = MathUtil.getStandVar(expVal1, firstPar);
                        double eref1 = Math.pow(tableOutput.getDeviation(estimator, sizeOfSample, 1),2)  / standVar1 ;
                        double standDev1 = Math.sqrt(standVar1);
                        
                        tableOutput.setMeanValue(estimator, sizeOfSample, 1, expVal1);
                        tableOutput.setDeviation(estimator, sizeOfSample, 1, standDev1);
                        tableOutput.setEfficiency(estimator, sizeOfSample, 1, eref1);
                        
                        
                        break;
                    } else {
                        double[] secondPar = new double[input.getSizeOfEstimator()];
                        for (int i = 0; i < secondPar.length; i++) {
                            secondPar[i] = estimatorArray[i].getP2();
                        }
                        double expVal2 = MathUtil.getExpVal(secondPar);
                        double standVar2 = MathUtil.getStandVar(expVal2, secondPar);
                        double eref2 = Math.pow(tableOutput.getDeviation(estimator, sizeOfSample, 2),2)  / standVar2 ;
                        double standDev2 = Math.sqrt(standVar2);
                        
                        tableOutput.setMeanValue(estimator, sizeOfSample, 2, expVal2);
                        tableOutput.setDeviation(estimator, sizeOfSample, 2, standDev2);
                        tableOutput.setEfficiency(estimator, sizeOfSample, 2, eref2);
                    }       
                } // END for (int sizeOfSample : input.getSizeOfSample())
            }// END for (EstimatorBuilder estimatorBuilder : input.getEstimators())
            
            tableOutputs.add(tableOutput);
        }// END for (TableInput input : tableInputs)
    } // END count()
}
