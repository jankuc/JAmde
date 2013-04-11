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
public class KolmCramEstimator extends Estimator{
    
    public KolmCramEstimator(int p, int q, int m) {
        this.par = new ArrayList<>();
        this.par.add((double) p); 
        this.par.add((double) q);
        this.par.add((double) m);
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        int p = (int) Math.round(this.par.get(0));
        int q = (int) Math.round(this.par.get(1));
        int m = (int) Math.round(this.par.get(2));
        double dist = 0;
        double y;
        
        double[] d = new double[2*data.length];
        double h = Math.max(2 * data.length - m, 0);

        Arrays.sort(data);
        
        for (int i = 0; i < data.length; i++) {
            y = distr.getFunctionValue(data[i]);
            d[i] = Math.pow(Math.abs((i + 1.0) / data.length - y), ((double) p) / q);
            d[2 * data.length - 1 - i] = Math.pow(Math.abs(((double) i) / data.length - y), ((double) p) / q);
        }

        Arrays.sort(d); // nebo naopak?
        
        for (int i = d.length - 1; i >= h; i--) {
            dist = dist + d[i];
        }
        
        dist = dist / m;
        return dist;
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KC}^\\frac{p}{q}_m, p="+OtherUtils.num2str(getPar(0)) + ", \\quad q="+OtherUtils.num2str(getPar(1)) + ", \\quad m=" +OtherUtils.num2str(getPar(2)) + "$");
    }
    
}