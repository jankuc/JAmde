/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.estimator.Estimator;
import java.util.ArrayList;

/**
 *
 * @author honza
 */
public class TableOutput {
    private ArrayList<Estimator> estimators;
    private ArrayList<Integer> sizeOfSample;
    private parameterStatistics[][][] parameter;

    class parameterStatistics {
        protected double meanValue;
        protected double deviation;
        protected double efficiency;
        protected double error;
        
        public parameterStatistics(){
            meanValue = 0;
            deviation = 0;
            efficiency = 0;
            error = 0;
        } 
    }

    // for outer world there are parameters 1 and 2, here it is 0 and 1. 
    
    public TableOutput(ArrayList<Estimator> estimators, ArrayList<Integer> sizeOfSample, int numOfPars) {
        this.estimators = estimators;
        this.sizeOfSample = sizeOfSample;
        parameter = new parameterStatistics[estimators.size()][sizeOfSample.size()][numOfPars-1];
    }
    
    public void setMeanValue(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].meanValue = value;
    }

    public void setDeviation(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].deviation = value;
    }
    
    public void setEfficiency(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].efficiency = value;
    }

    public void setError(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].error = value;
    }

    public double getMeanValue(Estimator e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].meanValue;
    }
    
    public double getDeviation(Estimator e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].deviation;
    }
    
    public double getEfficiency(Estimator e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].efficiency;
    }

    public double getError(Estimator e, int sizeOfSample, int par) {
        return parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1].error;
    }

}
