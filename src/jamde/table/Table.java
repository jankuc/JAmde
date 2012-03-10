/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.distribution.*;
import jamde.estimator.*;
import java.io.File;
import java.util.ArrayList;
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
        DistributionBuilder distBuilder = new DistributionBuilder(estType, estPar, estPar, estPar);
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
                    input.setContaminated(null);
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
    
    
}
