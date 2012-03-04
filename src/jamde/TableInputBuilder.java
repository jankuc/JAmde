/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

import jamde.estimator.EstimatorBuilder;
import jamde.distribution.DistributionBuilder;
import jamde.distribution.Distribution;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author honza
 */
public class TableInputBuilder {
    private ArrayList<TableInput> tableInputs;

    public ArrayList<TableInput> getTableInputs() {
        return tableInputs;
    }

    public TableInputBuilder(ArrayList<TableInput> tableInputs) {
        this.tableInputs = tableInputs;
    }
    
    public void loadFromFile(File confFile) throws Exception {
        System.out.println("You have opened configuration file " + confFile.toString() + "\n");

        tableInputs = new  ArrayList<TableInput>();
        TableInput input = new TableInput();
        Scanner sc = new Scanner(confFile);
        String sContaminated, sContaminating, estType = "";
        double contaminatedPar1 = 0, contaminatedPar2 = 1, contaminatedPar3 = 0, contaminatingPar1 = 0, contaminatingPar2 = 1, contaminatingPar3 = 1, estPar = 0;
        ArrayList<Integer> sizeOfSample = new ArrayList<Integer>();
        ArrayList<EstimatorBuilder> estimators = new ArrayList<EstimatorBuilder>();
        EstimatorBuilder e = new EstimatorBuilder(estType, estPar);
        DistributionBuilder distBuilder = new DistributionBuilder(estType, estPar, estPar, estPar);
        Distribution distr;
        int lineNumber = 1;
        
        while (sc.hasNext()) {    
            if (sc.hasNext("#")) {
                if (!(input.getContaminated()==null)) {
                    tableInputs.add(input);
                }
                sizeOfSample.clear();
                estimators.clear();
                input = new TableInput();
                
                lineNumber = 1;
                sc.next();
                
            } else if (lineNumber == 1) {
                sContaminated = sc.next();
                contaminatedPar1 = sc.nextDouble();
                contaminatedPar2 = sc.nextDouble();
                contaminatedPar3 = sc.nextDouble();
                distBuilder.setDistribution(sContaminated, contaminatedPar1, contaminatedPar2, contaminatedPar3);
                distr = distBuilder.getDistribution();
                input.setContaminated(distr);
                lineNumber++;
            } else if (lineNumber == 2) {
                sContaminating = sc.next();
                contaminatingPar1 = sc.nextDouble();
                contaminatingPar2 = sc.nextDouble();
                contaminatingPar3 = sc.nextDouble();
                distBuilder.setDistribution(sContaminating, contaminatingPar1, contaminatingPar2, contaminatingPar3);
                distr = distBuilder.getDistribution();
                input.setContaminating(distr);
                lineNumber++;
            } else if (lineNumber == 3) {
                input.setContamination(sc.nextDouble());
                lineNumber++;
            } else if (lineNumber == 4) {
                input.setParamsCounted(sc.next());
                input.setSizeOfEstimator(sc.nextInt());
                lineNumber++;
            } else if (lineNumber == 5) {
                while (sc.hasNextInt()) {
                    sizeOfSample.add(sc.nextInt());
                }
                input.setSizeOfSample(sizeOfSample);
                lineNumber++;
            } else if (lineNumber == 6) {
                estType = sc.next();
                estPar = sc.nextDouble();
                e = new EstimatorBuilder(estType, estPar);
                estimators.add(e);
                input.setEstimators(estimators);
            }
        }
    }
}
