package jamde.table;

import jamde.estimator.EstimatorBuilder;
import java.util.ArrayList;

/**
 * Class is used as an inner class in Table. It is used to hold all the estimated
 * parameters. It is then used as a source of data by printing procedures.
 * 
 * @author kucerj28@fjfi.cvut.cz
 */
public class TableRawOutput {
    private ArrayList<EstimatorBuilder> estimators;
    private ArrayList<Integer> sizeOfSample;
    private double[][][] estimatedParameter;

    // for outer world there are parameters 1 and 2, here it is 0 and 1. 
    
    /**
     * 
     * @param estimators
     * @param sizeOfSample
     * @param numOfPars -- number of estimated parameters
     */
    public TableRawOutput(ArrayList<EstimatorBuilder> estimators, ArrayList<Integer> sizeOfSample, int numOfPars) {
        this.estimators = estimators;
        this.sizeOfSample = sizeOfSample;
        this.estimatedParameter = new double[estimators.size()][sizeOfSample.size()][numOfPars];
        for (int i = 0; i < estimators.size(); i++){
            for (int j = 0; j < sizeOfSample.size(); j++){
                for (int k = 0; k < numOfPars; k++) {
                    estimatedParameter[i][j][k] = Double.NaN;
                }
            }
        }
        
    }
    
    /**
     * estimatedParameters[e][sizeOfSample][par] = value
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @param value
     */
    public void setEstimatedParameter(EstimatorBuilder e, int sizeOfSample, int par, double value) {
        estimatedParameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1] = value;
    }


    /**
     * 
     * @param e
     * @param sizeOfSample
     * @param par
     * @return parameterStatistics[e][sizeOfSample][par].meanValue
     */
    public double getEstimatedParameter(EstimatorBuilder e, int sizeOfSample, int par) {
        return estimatedParameter[estimators.indexOf(e)][this.sizeOfSample.indexOf(sizeOfSample)][par-1];
    }
}
