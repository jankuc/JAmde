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
public class KolmCramRandBetaEstimator extends KolmCramRand{
    private double b;
    
    public KolmCramRandBetaEstimator(int p, int q, double b, double k, double exp) {
        super(p, q, k, exp);
        this.b = b;
    }

    KolmCramRandBetaEstimator() {
        this(2,1,1,1,1);
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        return super.countDistance(distr, data, 2 * Math.pow(data.length, b));
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KCRB}^\\frac{p}{q}_{2n^\\beta, k}, p=" 
                + OtherUtils.num2str(p) + ", \\quad q="+OtherUtils.num2str(q) 
                + ", \\quad \\beta=" +OtherUtils.num2str(b) + ", \\quad k=" 
                + OtherUtils.num2str(k) + "$");
    }

    @Override
    public String toString() {
        return "KolmCramRandBetaEstimator{" + '}';
    }
}
