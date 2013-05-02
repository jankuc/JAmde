/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.OtherUtils;
import jamde.distribution.Distribution;


/**
 *
 * @author honza
 */
public class KolmCramRandMEstimator extends KolmCramRand {
    private double m;
    
    public KolmCramRandMEstimator(int p, int q, int m, double k, double exp) {
        super(p, q, k, exp);
        this.m = m;
    }

    KolmCramRandMEstimator() {
        this(2,1,1,1,1);
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        return super.countDistance(distr, data, m);
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KCR}^\\frac{p}{q}_{m, k}, p="+OtherUtils.num2str(p) 
                + ", \\quad q="+OtherUtils.num2str(q) + ", \\quad m=" 
                + OtherUtils.num2str(m) + ", \\quad k=" +OtherUtils.num2str(k) + "$");
    }
    
}
