/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.distribution.Distribution;

/**
 *
 * @author kucerj28@fjfi.cvut.cz
 */
public class LeCamEstimator extends Estimator{

    LeCamEstimator(double par) {
        setPar(par);
    }

    @Override
    public double countDistance(Distribution distr, double[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
