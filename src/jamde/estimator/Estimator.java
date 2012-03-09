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
    
    
}
