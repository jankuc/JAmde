/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.distribution.Distribution;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author honza
 */
public class KolmCramRandBetaEstimator extends Estimator{
    
    int[] selection;
    
    public KolmCramRandBetaEstimator(int p, int q, double b, double k, double exp, int[] selection) {
        this.par = new ArrayList<>();
        this.par.add((double) p); 
        this.par.add((double) q);
        this.par.add( b);
        this.par.add((double) k);
        this.par.add( exp);
        this.selection = selection;
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        int p = (int) Math.round(getPar(0));
        int q = (int) Math.round(getPar(1));
        double m = 2* Math.pow(data.length, getPar(2)); // m = 2*n^b
        int k = (int) Math.round(getPar(3));
        double exp = getPar(4);
        double dist = 0;
        double y;
        
        double[] d = new double[2*data.length];
        double h = Math.min(m-2,2 * data.length - 1);

        for (int i = 0; i < data.length; i++) {
            y = distr.getFunctionValue(data[i]);
            d[i] = Math.pow(Math.abs((i + 1) / data.length - y), (double) p / q);
            d[2 * data.length - 1 - i] = Math.pow(Math.abs(i / data.length - y), (double) p / q);
        }

        Arrays.sort(d); // nebo naopak?
        
        int U;
        for (int i = 0; i <= h; i++) {
            U = selection[i];
            dist = dist + Math.pow(2 * data.length - U, exp) * d[U];
        }
        

        dist = dist / Math.pow(m, exp + 1);
        dist = dist + d[2* data.length - 1] / (k * m);
        
        return dist;
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KCRB}^\\frac{p}{q}_{2n^\\beta, k}, p="+this.getPar(0) + ", \\quad q="+this.getPar(1) + ", \\quad \\beta=" +this.getPar(2) + ", \\quad k=" +this.getPar(3) + "$");
    }
    
}
