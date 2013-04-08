/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.OtherUtils;
import jamde.distribution.Distribution;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author honza
 */
public class KolmCramRandEstimator extends Estimator{
    
    int[] selection;
    
    public KolmCramRandEstimator(int p, int q, int m, double k, double exp, int[] selection) {
        this.par = new ArrayList<>();
        this.par.add((double) p); 
        this.par.add((double) q);
        this.par.add((double) m);
        this.par.add((double) k);
        this.par.add( exp);
        this.selection = selection;
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        int p = (int) Math.round(getPar(0));
        int q = (int) Math.round(getPar(1));
        int m = (int) Math.round(getPar(2)); 
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
        return("$ \\mathrm{KCR}^\\frac{p}{q}_{m, k}, p="+OtherUtils.num2str(getPar(0)) + ", \\quad q="+OtherUtils.num2str(getPar(1)) + ", \\quad m=" +OtherUtils.num2str(getPar(2)) + ", \\quad k=" +OtherUtils.num2str(getPar(3)) + "$");
    }
    
}
