/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.distribution.Distribution;
import java.util.ArrayList;

/**
 * Generalized Cramer. Instead of (F-Fn)^2 it is (F-Fn)^(p/q)
 * 
 * @author honza
 */
public class CramerRationalPowerEstimator extends Estimator{

    public CramerRationalPowerEstimator(int p, int q) {
        this.par = new ArrayList<>();
        this.par.add((double) p); 
        this.par.add((double) q);
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        int p = (int) Math.round(this.par.get(0));
        int q = (int) Math.round(this.par.get(1));
        double dist = 0;
        double y;
        double a;
        double b;

        for (int i = 0; i < data.length; i++) {
            y = distr.getFunctionValue(data[i]);

            if ((y - i) / data.length < 0) {
                a = -1.0;
            } else {
                a = 1.0;
            }
            if (y - (i + 1) / data.length < 0) {
                b = -1.0;
            } else {
                b = 1.0;
            }
            dist = dist + a * Math.pow(Math.abs(y - (i) / data.length), (double) p / q + 1) - b * Math.pow(Math.abs(y - (i + 1) / data.length), (double) p / q + 1);
        }
        dist = dist / (p / q + 1);
        
        return dist;
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{Cramer}^\\frac{p}{q}, p="+this.getPar(0) + ", \\quad q="+this.getPar(1) + "$");
    }
    
}
