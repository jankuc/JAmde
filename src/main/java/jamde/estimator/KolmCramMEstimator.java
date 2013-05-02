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
public class KolmCramMEstimator extends KolmCram{
    private double m;
    
    public KolmCramMEstimator(int p, int q, int m) {
        super(p,q);
        this.m = m;
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        return super.countDistance(distr, data, m);
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KC}^\\frac{p}{q}_m, p="+OtherUtils.num2str(p) + ", \\quad q="+OtherUtils.num2str(q) + ", \\quad m=" +OtherUtils.num2str(m) + "$");
    }

   
    
}