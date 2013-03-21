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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author kucerj28@fjfi.cvut.cz
 */
public class Table {
    private ArrayList<TableInput> tableInputs = new ArrayList<>();
    private ArrayList<TableOutput> tableOutputs = new ArrayList<>(); // elements in the list are in the same order as the elements in the input
    private ArrayList<TableRawOutput> tableRawOutputs = new ArrayList<>();
    
    /**
     * 
     * @return tableInputs
     */
    public ArrayList<TableInput> getTableInputs() {
        return tableInputs;
    }

    /**
     * 
     * @param tableInput
     */
    public void addTableInput(TableInput tableInput) {
        this.tableInputs.add(tableInput);
    }

    /**
     * 
     * @return tableOutputs
     */
    public ArrayList<TableOutput> getTableOutputs() {
        return tableOutputs;
    }

    /**
     * 
     * @param tableOutputs
     */
    public void setTableOutputs(ArrayList<TableOutput> tableOutputs) {
        this.tableOutputs = tableOutputs;
    }

    /**
     * 
     * @param tableOutput
     */
    public void addTableOutput(TableOutput tableOutput) {
        this.tableOutputs.add(tableOutput);
    }

    /**
     * 
     * @return tableRawOutput 
     */
    public ArrayList<TableRawOutput> getTableRawOutputs() {
        return tableRawOutputs;
    }

    /**
     * 
     * @param tableRawOutputs 
     */
    public void setTableRawOutputs(ArrayList<TableRawOutput> tableRawOutputs) {
        this.tableRawOutputs = tableRawOutputs;
    }

    
    
    
    /**
     * Loads <b>inputs</b> from configuration file. It finds beginning of the 
     * input which is marked with # and calls <b>loadInputFromFile</b> on that 
     * input.
     * 
     * @param confFile
     * @throws Exception
     */
    public void loadInputsFromFile(File confFile) throws Exception {
        System.out.println("You have opened configuration file " + confFile.toString() + "\n");

        ArrayList<TableInput> inputs = new ArrayList<>();
        TableInput input;

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

    /**
     * Loads one input from the configuration file and writes it into 
     * <b>input</b>.
     * 
     * @param sc
     * @return
     * @throws Exception 
     */
    private TableInput loadInputFromFile(Scanner sc) throws Exception {
        TableInput input = new TableInput();
        String sContaminated, sContaminating, estType;
        double contaminatedPar1 = 0, contaminatedPar2 = 1, contaminatedPar3 = 0, contaminatingPar1 = 0, contaminatingPar2 = 1, contaminatingPar3 = 1, estPar = 0;
        ArrayList<Integer> sizeOfSample = new ArrayList<>();
        ArrayList<EstimatorBuilder> estimators = new ArrayList<>();
        EstimatorBuilder e;
        input.setSizeOfEstimator(0);
        DistributionBuilder distBuilder = new DistributionBuilder();
        double[] errProb;
        ArrayList<double[]> errProbs = new ArrayList<>();
        String line;

        int lineNumber = 1;

        while (sc.hasNext()) {
            if (sc.hasNext("#")) {
                if ((input.getSizeOfEstimator() != 0)) {
                    return input;
                }
                sc.next();
            } else if (lineNumber == 1) {
                sc.nextLine(); // sc is still at the end of #-line, so the first nextLine() loads just ""
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

    /**
     * In a few nested cycles counts all the needed statistics specified in 
     * <b>input</b> and writes them into <b>output</b>. For the enumeration 
     * it uses array of threads, each of them takes part of the enumeration.
     * 
     * @param numOfThreads
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public int count(int numOfThreads) throws FileNotFoundException, InterruptedException {
        System.out.println("Enumeration has begun");
        boolean printDist = false;
        /* 
         * TODO vytisk distancni matice printDist dat do parametru funkce a nasledne i do vstupu programu z cmdline
         */
        int offset;
        /*
         * TODO pri nacitani umisteni tabulky toto umisteni vytvori
         * TODO vytvorit funkci pro vykreslovani vzdalenostnich obrazku
         */
        int numOfTables = tableInputs.size();
        int tableInProgress = 0;
        for (TableInput input : tableInputs) { // cycle over all tables
            tableInProgress++;
            Long tableStartTime = System.currentTimeMillis();
            int numOfPars = 1;
            if (input.getParamsCounted().equals("both")) {
                numOfPars = 2;
            } else if (input.getParamsCounted().equals("all")) {
                numOfPars = 3;
            }
            TableOutput tableOutput = new TableOutput((ArrayList<EstimatorBuilder>) input.getEstimators(), (ArrayList<Integer>) input.getSizeOfSample(), numOfPars);
            TableRawOutput tableRawOutput = new TableRawOutput(input.getEstimators(), (ArrayList<Integer>) input.getSizeOfSample(), numOfPars, input.getSizeOfEstimator());
            EstimatorBuilder estimatorBuilderL1 = input.getEstimators().get(0);
            for (EstimatorBuilder estimatorBuilder : input.getEstimators()) { //cycle over all estimators in one table (lines of the table)
                Distribution[] estimatorArray = new Distribution[input.getSizeOfEstimator()]; // for every dsitribution in this array, we will find parameters which minimize the distance from this distribution to the current data.
                for (int i = 0; i < estimatorArray.length; i++) {
                    DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1(), input.getContaminated().getP2(), input.getContaminated().getP3());
                    estimatorArray[i] = dB.getDistribution();
                }
                for (int sizeOfSample : input.getSizeOfSample()) { // cycle over all the sizes of dataArray. (columns in the table) [20, 50, 100, 200, 500]
                    if (printDist){ // DISTANCE MATRIX
                      printDistanceFunction(input, estimatorBuilder.getEstimator(), sizeOfSample, new File("./distances")); // TODO udelat lepe soubor.
                      return 1;
                    }
                    // Preparation for Threads:
                    int threadLoad = (int) (estimatorArray.length / numOfThreads); // Load which will be covered by each Thread 1000/30
                    int leftover = estimatorArray.length - threadLoad * numOfThreads; // It will be distributed between Threads 1000 - 1000/30
                    int[] threadLoads = new int[numOfThreads];
                    for (int i = 0; i < threadLoads.length; i++) {
                        if (leftover > 0) {
                            threadLoads[i] = threadLoad + 1;
                            leftover--;
                        } else {
                            threadLoads[i] = threadLoad;
                        }
                    }
                    // Enumeration
                    CountThread[] threadArray = new CountThread[numOfThreads]; // Array of Threads
                    Distribution[][] threadResult = new Distribution[numOfThreads][]; // threadResult holds the distribution, which is result of the enumeration of eash thread
                    DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1(), input.getContaminated().getP2(), input.getContaminated().getP3());
                    for (int i = 0; i < threadArray.length; i++) { //0:numOfThreads-1
                        threadArray[i] = new CountThread(input, threadLoads[i], estimatorBuilder, sizeOfSample);
                        threadResult[i] = new Distribution[threadLoads[i]];
                    }
                    for (int i = 0; i < threadArray.length; i++) { // Starts the count in all the threads
                        threadResult[i] = threadArray[i].startCount();
                    }
                    for (int i = 0; i < threadArray.length; i++) {
                        threadArray[i].join();
                        for (int j = 0; j < threadResult[i].length; j++) { // Copying of result from threads to estimatorArray
                            offset = i * threadLoads[i]; // Data from 2D array threadResult is copied into 1D estimatorArray, that's the reason for the offset
                            estimatorArray[offset + j] = threadResult[i][j];
                        }
                    }
                    // End of enumeration
                    writeIntoTableOutput(estimatorArray, input, tableOutput, estimatorBuilder, sizeOfSample);
                    writeIntoTableRawOutput(estimatorArray, input, tableRawOutput , estimatorBuilder, sizeOfSample);
                } // END for (int sizeOfSample : input.getSizeOfSample())
                System.out.println("Estimator " + estimatorBuilder.getType() + "(" + estimatorBuilder.getPar() + ") has ended.");
            } // END for (EstimatorBuilder estimatorBuilder : input.getEstimators())
            tableOutputs.add(tableOutput);
            tableRawOutputs.add(tableRawOutput);
            Long tableEndTime = System.currentTimeMillis();
            Long tableTime = tableEndTime - tableStartTime;
            System.out.println("Table " + tableInProgress + "/" + numOfTables + " has ended. It took " + MathUtil.Long2time(tableTime) + ". It could take another " + MathUtil.Long2time((numOfTables - tableInProgress) * tableTime));
        }// END for (TableInput input : tableInputs)
        System.out.println("Enumeration has ended.");
        return 0;
    } // END count()

    /**
     * Encapsulates all the printing procedures for the "Classic" table. 
     * 
     * @param fileName
     * @throws IOException
     */

    /**
     * Counts and writes values of all the needed statistics into <b>output</b>. 
     * <b>input</b> and <b>output</b> are passed as parameters, because in 
     * <b>Table</b> there are lists of them, so now they now what are they.
     * 
     * @param estimatorArray
     * @param input
     * @param output
     * @param estimatorBuilder
     * @param sizeOfSample
     * @return 
     */
    private TableOutput writeIntoTableOutput(Distribution[] estimatorArray, TableInput input, TableOutput output, EstimatorBuilder estimatorBuilder, int sizeOfSample) {
        int numOfPars = 1;
        if (input.getParamsCounted().equals("both")) {
            numOfPars = 2;
        }
        if (input.getParamsCounted().equals("all")) {
            numOfPars = 3;
        }
        EstimatorBuilder estimatorBuilderMLE = input.getEstimators().get(0);
        if (input.getParamsCounted().equals("both") || input.getParamsCounted().equals("first") || input.getParamsCounted().equals("all")) {
            double[] firstPar = new double[input.getSizeOfEstimator()];
            for (int i = 0; i < firstPar.length; i++) {
                firstPar[i] = estimatorArray[i].getP1();
            }
            double expVal1 = MathUtil.getExpVal(firstPar);
            double standVar1 = MathUtil.getStandVar(input.getContaminated().getP1(), firstPar);
            double standDev1 = Math.sqrt(standVar1);
            output.setMeanValue(estimatorBuilder, sizeOfSample, 1, expVal1);
            output.setDeviation(estimatorBuilder, sizeOfSample, 1, standDev1);
            double eref1 = Math.pow(output.getDeviation(estimatorBuilderMLE, sizeOfSample, 1), 2) / standVar1; // radim pocital  eef =  varX/varL1 (bez odmocniny)
            output.setEfficiency(estimatorBuilder, sizeOfSample, 1, eref1);
        }
        if (input.getParamsCounted().equals("both") || input.getParamsCounted().equals("second") || input.getParamsCounted().equals("all")) {
            double[] secondPar = new double[input.getSizeOfEstimator()];
            for (int i = 0; i < secondPar.length; i++) {
                secondPar[i] = estimatorArray[i].getP2();
            }
            double expVal2 = MathUtil.getExpVal(secondPar);
            double standVar2 = MathUtil.getStandVar(input.getContaminated().getP2(), secondPar);
            double standDev2 = Math.sqrt(standVar2);
            if (numOfPars != 3){ // 2 or 1 parameters were counted
                output.setMeanValue(estimatorBuilder, sizeOfSample, numOfPars, expVal2);
                output.setDeviation(estimatorBuilder, sizeOfSample, numOfPars, standDev2);
                double eref2 = Math.pow(output.getDeviation(estimatorBuilderMLE, sizeOfSample, numOfPars), 2) / standVar2;
                output.setEfficiency(estimatorBuilder, sizeOfSample, numOfPars, eref2);
            } else {
                numOfPars = 2;
                output.setMeanValue(estimatorBuilder, sizeOfSample, numOfPars, expVal2);
                output.setDeviation(estimatorBuilder, sizeOfSample, numOfPars, standDev2);
                double eref2 = Math.pow(output.getDeviation(estimatorBuilderMLE, sizeOfSample, numOfPars), 2) / standVar2;
                output.setEfficiency(estimatorBuilder, sizeOfSample, numOfPars, eref2);
                numOfPars = 3;
            }
        }
        if (input.getParamsCounted().equals("all") || input.getParamsCounted().equals("third")) { // numOfPars == 3;
            double[] thirdPar = new double[input.getSizeOfEstimator()];
            for (int i = 0; i < thirdPar.length; i++) {
                thirdPar[i] = estimatorArray[i].getP3();
            }
            double expVal3 = MathUtil.getExpVal(thirdPar);
            double standVar3 = MathUtil.getStandVar(input.getContaminated().getP3(), thirdPar);
            double standDev3 = Math.sqrt(standVar3);
            output.setMeanValue(estimatorBuilder, sizeOfSample, numOfPars, expVal3);
            output.setDeviation(estimatorBuilder, sizeOfSample, numOfPars, standDev3);
            double eref3 = Math.pow(output.getDeviation(estimatorBuilderMLE, sizeOfSample, numOfPars), 2) / standVar3;
            //eref2 = Math.sqrt(eref2);
            output.setEfficiency(estimatorBuilder, sizeOfSample, numOfPars, eref3);
        }
        return output;
    }
    
    /**
     * Counts and writes values of all the needed statistics into <b>rawOutput</b>. 
     * <b>input</b> and <b>rawOutput</b> are passed as parameters, because in 
     * <b>Table</b> there are lists of them, so now they now what are they.
     * 
     * @param estimatorArray
     * @param input
     * @param output
     * @param estimatorBuilder
     * @param sizeOfSample
     * @return 
     */
    private TableRawOutput writeIntoTableRawOutput(Distribution[] estimatorArray, TableInput input, TableRawOutput rawOutput, EstimatorBuilder estimatorBuilder, int sizeOfSample) {
        int numOfPars = 1;
        if (input.getParamsCounted().equals("both")) {
            numOfPars = 2;
        }
        if (input.getParamsCounted().equals("all")) {
            numOfPars = 3;
        }
        if (input.getParamsCounted().equals("both") || input.getParamsCounted().equals("first") || input.getParamsCounted().equals("all")) {
            for (int i = 0; i < estimatorArray.length; i++) {
                rawOutput.setEstimatedParameter(estimatorBuilder, sizeOfSample, 1, i, estimatorArray[i].getP1());
            }
        }
        if (input.getParamsCounted().equals("both") || input.getParamsCounted().equals("second") || input.getParamsCounted().equals("all")) {
            if (numOfPars != 3){ // 2 or 1 parameters were counted
                for (int i = 0; i < estimatorArray.length; i++) { //
                    rawOutput.setEstimatedParameter(estimatorBuilder, sizeOfSample, numOfPars, i, estimatorArray[i].getP2());
                }
            } else {
                for (int i = 0; i < estimatorArray.length; i++) {
                    rawOutput.setEstimatedParameter(estimatorBuilder, sizeOfSample, 2, i, estimatorArray[i].getP2());
                }
            }
        }
        if (input.getParamsCounted().equals("all") || input.getParamsCounted().equals("third")) { // numOfPars == 3;
            for (int i = 0; i < estimatorArray.length; i++) {
                rawOutput.setEstimatedParameter(estimatorBuilder, sizeOfSample, numOfPars, i, estimatorArray[i].getP3());
            }
        }
//        for (int i = 0; i < estimatorArray.length; i++) { //
//            rawOutput.setEstimatedParameter(i, sizeOfSample, 1, estimatorArray[i].getP1());
//            rawOutput.setEstimatedParameter(i, sizeOfSample, 2, estimatorArray[i].getP2());
//            rawOutput.setEstimatedParameter(i, sizeOfSample, 3, estimatorArray[i].getP3());
//        }
        return rawOutput;
    }
    
    /**
     * Creates file ./distances and writes all the measured distances into it. 
     * It is good to load it into matlab and plot it with: <br>
     * C = importdata('./distances'); imagesc(C); colorbar; <br>
     * 
     * @param input
     * @param estimator
     * @param sizeOfSample
     */
    public void printDistanceFunction(TableInput input, Estimator estimator, int sizeOfSample, File file) {
        double[] dataArray = createData(input, sizeOfSample);
        PrintWriter w;
        try {
            w = new PrintWriter(file);
            double distance;
            //Distribution d = new NormalDistribution(0, 1);
            DistributionBuilder distrBuild = new DistributionBuilder(input.getContaminated());
            Distribution distr = distrBuild.getDistribution();
            
            double Nx = 500; // number of points in axis x (P1)
            double Ny = 500; // number of points in axis y (P2)
            
            double x = distr.lowP1+7 , y;
            double dx = (distr.upP1 - distr.lowP1 -14 ) / Nx ;
            double dy = (distr.upP2 - distr.lowP2 -14 ) / Ny;
            while (x < distr.upP1+7) {
                y = distr.lowP2;
                while (y < distr.upP2-14) {
                    distr.setParameters(x, y, 0);
                    distance = estimator.countDistance(distr, dataArray);
                    if (distance < Double.POSITIVE_INFINITY) {
                        w.format(" %.4f ", distance);
                    }
                    else {
                        w.format(" inf ");
                    }
                    y = y + dy;
                }
                w.format(" \n ");
                x = x + dx;
            }
            w.format(" \n ");
            w.close();
            System.out.println("Vypsani Distance souboru skonceno.");
        } catch (FileNotFoundException ex) {
            /*
             * TODO catch the exception
             */
        }
    }
    
    /**
     * According to <b>input</b> creates dataset by: <br>
     * loading from file; <br>
     * making a mixture of distributions; <br>
     * making distribution with errors in orders of magnitude. <br>
     * 
     * @param input
     * @param sizeOfSample
     * @return
     */
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
