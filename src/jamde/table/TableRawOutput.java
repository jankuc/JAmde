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
    private int K;
    private double[][][][] estimatedParameter;
    

    // for outer world there are parameters 1 and 2, here it is 0 and 1. 
    
    /**
     * 
     * @param k ... sizeOfEstimator
     * @param sizeOfSample
     * @param numOfPars -- number of estimated parameters
     */
    public TableRawOutput(ArrayList<EstimatorBuilder> estimators, ArrayList<Integer> sizeOfSample, int numOfPars, int K) {
        this.estimators = estimators;
        this.sizeOfSample = sizeOfSample;
        this.K = K;
        estimatedParameter = new double[estimators.size()][sizeOfSample.size()][numOfPars][K];
        for (int i = 0; i < estimators.size(); i ++) {
            for (int j = 0; j < sizeOfSample.size(); j++) {
                for (int k = 0; k < numOfPars; k++) {
                    for (int l = 0; l < K; l++) {
                        estimatedParameter[i][j][k][l] = Double.NaN;
                    }
                }
            }
        }
    }
    
    /**
     * estimatedParameters[estimators][sizeOfSample][par][k] = value
     * 
     * @param estimator...type of estimator 
     * @param sizeOfSample... size of sample which was estimated
     * @param par... one of {1,2,3}. Is estimated parameter of the pdf
     * @param k...number of estimation in the series of K estimations
     * @param value... value of the estimated parameter
     */
    public void setEstimatedParameter(EstimatorBuilder estimator, int n, int par, int k, double value) {
        int estI = estimators.indexOf(estimator);
        int nI = sizeOfSample.indexOf(n);
        
        estimatedParameter[estimators.indexOf(estimator)][sizeOfSample.indexOf(n)][par-1][k] = value;
    }

    public double getEstimatedParameter(EstimatorBuilder estimator, int n, int par, int k) {
        return estimatedParameter[estimators.indexOf(estimator)][sizeOfSample.indexOf(n)][par-1][k];
    }
    
    
    
}
