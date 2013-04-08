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
public class KolmCramBetaEstimator extends Estimator{
    
    public KolmCramBetaEstimator(int p, int q, double b) {
        this.par = new ArrayList<>();
        this.par.add((double) p); 
        this.par.add((double) q);
        this.par.add((double) b);
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        int p = (int) Math.round(this.par.get(0));
        int q = (int) Math.round(this.par.get(1));
        double m = 2* Math.pow(data.length, this.par.get(2));
        double dist = 0;
        double y;
        
        double[] d = new double[2*data.length];
        double h = Math.max(2 * data.length - m, 0);

        for (int i = 0; i < data.length; i++) {
            y = distr.getFunctionValue(data[i]);
            d[i] = Math.pow(Math.abs((i + 1) / data.length - y), (double) p / q);
            d[2 * data.length - 1 - i] = Math.pow(Math.abs(i / data.length - y), (double) p / q);
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
        return("$ \\mathrm{KC}^\\frac{p}{q}_{2n^\\beta}, p="+OtherUtils.num2str(getPar(0)) + ", \\quad q="+OtherUtils.num2str(getPar(1)) + ", \\quad \\beta=" +OtherUtils.num2str(getPar(2)) + "$");
    }
    
}
