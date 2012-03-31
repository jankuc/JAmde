/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.estimator.EstimatorBuilder;
import java.util.ArrayList;

/**
 * Class is used as an inner class in Table. It is used to hold all the counted 
 * statistics. It is then used as a source of data by printing procedures.
 * Field parameter is ParameterStatistics[estimators][sizeOfSample][numOfPars]
 * 
 * @author kucerj28
 */
public class TableOutput {
    private ArrayList<EstimatorBuilder> estimators;
    private ArrayList<Integer> sizeOfSample;
    private ParameterStatistics[][][] parameter;

    /**
     * Inner class which has all the necessary statistics counted in it.
     * At this time supported are: meanValue, deviation, efficiency, error
     */
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
    
    /**
     * 
     * 
     * @param estimators
     * @param sizeOfSample
     * @param numOfPars
     */
    public TableOutput(ArrayList<EstimatorBuilder> estimators, ArrayList<Integer> sizeOfSample, int numOfPars) {
        this.estimators = estimators;
        this.sizeOfSample = sizeOfSample;
        this.parameter = new ParameterStatistics[estimators.size()][sizeOfSample.size()][numOfPars];
        for (int i = 0; i < estimators.size(); i++){
            for (int j = 0; j < sizeOfSample.size(); j++){
                for (int k = 0; k < numOfPars; k++) {
                    parameter[i][j][k] = new ParameterStatistics();
                }
            }
        }
        
    }
    
    /**
     * Sets value to the meanValue field of the parameterStatistics[e][sizeOfSample][par] 
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @param value
     */
    public void setMeanValue(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].meanValue = value;
    }

    /**
     * Sets value to the deviation field of the parameterStatistics[e][sizeOfSample][par] 
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @param value
     */
    public void setDeviation(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].deviation = value;
    }
    
    /**
     * Sets value to the efficiency field of the parameterStatistics[e][sizeOfSample][par] 
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @param value
     */
    public void setEfficiency(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].efficiency = value;
    }

    /**
     * Sets value to the error field of the parameterStatistics[e][sizeOfSample][par] 
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @param value
     */
    public void setError(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].error = value;
    }

    /**
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @return parameterStatistics[e][sizeOfSample][par].meanValue
     */
    public double getMeanValue(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].meanValue;
    }
    
    /**
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @return parameterStatistics[e][sizeOfSample][par].deviation
     */
    public double getDeviation(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].deviation;
    }
    
    /**
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @return parameterStatistics[e][sizeOfSample][par].efficiency
     */
    public double getEfficiency(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].efficiency;
    }

    /**
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @return parameterStatistics[e][sizeOfSample][par].error
     */
    public double getError(EstimatorBuilder e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].error;
    }

}
