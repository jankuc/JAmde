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
public class KolmCramRandFEstimator extends KolmCramRand{
    private double f;
    
    /**
     * 
     * m = 2*f*n
     * 
     * @param p
     * @param q
     * @param f... in (0,1) is part of 2n.
     * @param k
     * @param exp
     * @param selection 
     */
    public KolmCramRandFEstimator(int p, int q, double f, double k, double exp) {
        super(p,q,k,exp);
        this.f = f;
    }

    KolmCramRandFEstimator() {
        this(2,1,1,1,1);
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        return super.countDistance(distr, data,2* data.length * f);
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KCFR}^\\frac{p}{q}_{2fn, k}, p="+ OtherUtils.num2str(p)
                + ", \\quad q="+OtherUtils.num2str(q) + ", \\quad f="
                + OtherUtils.num2str(f) + ", \\quad k=" +OtherUtils.num2str(k) + "$");
    }

    @Override
    public String toString() {
        return "KolmCramRandFEstimator{" + '}';
    }
    
    
}
