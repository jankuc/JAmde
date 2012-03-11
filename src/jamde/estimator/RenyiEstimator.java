/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.distribution.Distribution;

/**
 *
 * @author honza
 */
public class RenyiEstimator extends Estimator {

    public RenyiEstimator(double par) {
        setPar(par);
    }

    @Override
    public double countDistance(Distribution distribution, double[] data) {
        double dist = 0.0;
        if (par == 0) {
            for (int i = 0; i < data.length; i++) {  // viz. Radim Demut (dipl. práce) str 38, (6.71)
                dist += Math.log(distribution.getfunctionValue(data[i]));
            }
            dist = 1 - dist / data.length;
            return dist;
        }
        if (distribution.toString().equals("Normal")){  // viz. Radim Demut (dipl. práce) str 38, (6.78)
            double mu = distribution.getP1();
            double sigma = distribution.getP2();
            for (int i = 0; i < data.length; i++) {
                dist += Math.exp(- par * Math.pow(data[i] - mu,2) / (2 * Math.pow(sigma, 2)));
            }
            dist = 1 - dist / (data.length * Math.pow(sigma, par / (1 + par)));
            return dist;
        } else { // viz. Radim Demut (dipl. práce) str 37, (6.57)
            throw new UnsupportedOperationException("Not supported yet.");   
        }
    }
    
}
