/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.MathUtil;
import jamde.distribution.Distribution;
import java.util.Arrays;

/**
 *
 * @author honza
 */
public class KolmogorovEstimator extends Estimator{

    public KolmogorovEstimator(){
        super.par = null;
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        double dist[] = {-1,-1,-1};
        double y;
        Arrays.sort(data);
        for (int i = 0; i < data.length; i++) {
            y = distr.getFunctionValue(data[i]);
            dist[1] = Math.abs(y - (((double) i)/data.length));
            dist[2] = Math.abs(y - (((double) (i+1))/data.length));
            dist[0] = MathUtil.max(dist);
        }
        return dist[0];
    }

    @Override
    public String getClassicTableName() {
        return ("Kolmogorov");
    }
    
}
