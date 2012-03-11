/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.estimator.EstimatorBuilder;
import java.util.ArrayList;

/**
 *
 * @author honza
 */
public class TableOutput {
    private ArrayList<EstimatorBuilder> estimators;
    private ArrayList<Integer> sizeOfSample;
    private ParameterStatistics[][][] parameter;

    private class ParameterStatistics {
        public double meanValue = 1;
        public double deviation = 1;
        public double efficiency = 1;
        public double error = 1;
        
        public ParameterStatistics(){
            meanValue = 1;
            deviation = 1;
            efficiency = 1;
            error = 1;
        } 
    }

    // for outer world there are parameters 1 and 2, here it is 0 and 1. 
    
    public TableOutput(ArrayList<EstimatorBuilder> estimators, ArrayList<Integer> sizeOfSample, int numOfPars) {
        this.estimators = estimators;
        this.sizeOfSample = sizeOfSample;
        this.parameter = new ParameterStatistics[estimators.size()][sizeOfSample.size()][numOfPars];
        for (int i = 0; i < estimators.size(); i++){
            for (int j = 0; j < sizeOfSample.size(); j++){
                for (int k = 0; k < numOfPars; k++) {
                    parameter[i][j][k] = new ParameterStatistics();
//                    parameter[i][j][k].deviation = 0;
//                    p3.efficiency = 0;
//                    p3.error = 0;
//                    p3.meanValue = 0;
                }
            }
        }
        
    }
    
    public void setMeanValue(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].meanValue = value;
    }

    public void setDeviation(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].deviation = value;
    }
    
    public void setEfficiency(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].efficiency = value;
    }

    public void setError(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].error = value;
    }

    public double getMeanValue(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].meanValue;
    }
    
    public double getDeviation(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].deviation;
    }
    
    public double getEfficiency(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].efficiency;
    }

    public double getError(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].error;
    }

}
