/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.distribution.Distribution;
import java.util.Arrays;

/**
 * Class is used for countDistance of KolmCramRandMEstimator and KolmCramRandBEstimator.
 *
 * @author honza
 */
public abstract class KolmCramRand extends KolmCram {

    protected double k;
    protected double exp;
    private double h = -1;
    private int[] selection;
    

    public KolmCramRand(int p, int q, double k, double exp) {
        super(p, q);
        this.k = k;
        this.exp = exp;
    }

    /**
     * Method is called by KolmCramMEstimator and KolmCramBEstimator, because they choose m diferently.
     * 
     * @param distr
     * @param data
     * @param m
     * @return 
     */
    @Override
    public double countDistance(Distribution distr, double[] data, double m) {

        double dist = 0;
        double y;
        
        int n = data.length;
        m = Math.min(m,n);
        double[] d = new double[2 * n];

        if (h == -1) { // selection wasn't initialized, so we have to do so
            h = Math.min(m - 2, 2 * n - 1);
            selection = new int[(int) Math.floor(h + 1)];
            for (int i = 0; i < selection.length; i++) {
                selection[i] = i;
            }
        }

        Arrays.sort(data);

        for (int i = 0; i < n; i++) {
            y = distr.getFunctionValue(data[i]);
            d[i] = Math.pow(Math.abs(((double) i + 1.0) / n - y), p / q);
            d[2 * n - 1 - i] = Math.pow(Math.abs(((double) i) / n - y), p / q);
        }

        Arrays.sort(d);

        int U;
        for (int i : selection) {// == for(j=0;j<selection.length;j++) { i = selection[j]
            U = i;
            dist = dist + Math.pow(2.0 * n - (double) U, exp) * d[U];
        }

        dist = dist / Math.pow(m, exp + 1.0);
        dist = dist + d[2 * n - 1] / (k * m);

        return dist;

    }
}
