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
public abstract class Estimator {
 
    protected double par;

    public double getPar() {
        return par;
    }

    public void setPar(double par) {
        this.par = par;
    }
    
    public abstract double countDistance(Distribution distr, double[] data);

    public Distribution minimalize(Distribution closestDistribution, double[] dataArray) {
        /*
         * TODO minimalizace
         */
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Distribution minimalizeFirstPar(Distribution closestDistribution, double[] dataArray) {
        /*
         * TODO minimalizace par1
         */
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Distribution minimalizeSeconPar(Distribution closestDistribution, double[] dataArray) {
        /*
         * TODO minimalizace par2
         */
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
}
