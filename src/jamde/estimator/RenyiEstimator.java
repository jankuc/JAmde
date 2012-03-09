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
        double dist = 0;
        if (distribution.toString().equals("Normal")){
            for (int i = 0; i < data.length; i++) {
                dist += Math.exp( - getPar() * Math.pow(data[i] - distribution.getP1(),2)) / (2 * Math.pow(distribution.getP2(), 2));
            }
            dist = 1 - dist / (data.length * Math.pow(distribution.getP2(), getPar() / (1 + getPar())));
            return dist;
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
            
        }
    }
    
}
