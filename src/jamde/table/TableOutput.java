/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.estimator.Estimator;
import java.lang.reflect.Array;
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
        protected double variance;
        protected double efficiency;
        protected double error;
        
        public parameterStatistics(){
            meanValue = 0;
            variance = 0;
            efficiency = 0;
            error = 0;
        } 
    }

    public TableOutput(ArrayList<Estimator> estimators, ArrayList<Integer> sizeOfSample, int numOfPars) {
        this.estimators = estimators;
        this.sizeOfSample = sizeOfSample;
        parameter = new parameterStatistics[estimators.size()][sizeOfSample.size()][numOfPars];
    }
    
    public void setMeanValue(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par].meanValue = value;
    }

    public void setVariance(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par].variance = value;
    }
    
    public void setEfficiency(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par].efficiency = value;
    }

    public void setError(Estimator e, int sizeOfSample, int par, double value) {
        parameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par].error = value;
    }


}
