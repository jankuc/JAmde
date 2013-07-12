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
 * Generalized Cramer. Instead of (F-Fn)^2 it is (F-Fn)^(p/q)
 * 
 * @author honza
 */
public class CramGenIntEstimator extends Estimator{

    public CramGenIntEstimator(int p, int q) {
        addPar((double) p); 
        addPar((double) q);
    }     

    @Override
    public double countDistance(Distribution distr, double[] data) {
        double p = getPar(0);
        double q = getPar(1);
        double a, b, y, dist = 0;

        Arrays.sort(data);

        for (int i = 0; i < data.length; i++) {
            y = distr.getFunctionValue(data[i]);

            if (y - ((i) * 1.0) / data.length < 0) {
                a = -1.0;
            } else {
                a = 1.0;
            }
            if (y - ((i + 1) * 1.0) / data.length < 0) {
                b = -1.0;
            } else {
                b = 1.0;
            }
            dist = dist + a * Math.pow(Math.abs(y - ((double) i) / data.length), p/q + 1) - b * Math.pow(Math.abs(y - ((double) i + 1.0) / data.length), p/q + 1);
        }
        dist =  dist / (((double) p) / q + 1.0);

        return dist;
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KC}^\\frac{p}{q}_{\\mathrm{int}}, p="+OtherUtils.num2str(getPar(0)) + ", \\quad q="+OtherUtils.num2str(getPar(1)) + "$");
    }
    
}
