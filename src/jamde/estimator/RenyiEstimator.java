/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.MathUtil;
import jamde.distribution.Distribution;

/**
 * RenyiEstimator extends Estimator. Implements countDistance().
 * 
 * @author kucerj28
 */
public class RenyiEstimator extends Estimator {

    public RenyiEstimator(double par) {
        setPar(par);
    }

    /*
     * TODO Renyi estimator jako interface?
     */
    
    /**
     * Counts distance from distribution to the data. The distance counted 
     * depends on the family of the distribution. <br>
     * We always return not the counted distance, but 1-<b>dist</b>, because 
     * <b>dist</b> is in (0,1) and all the optimization procedures are for 
     * minimization.
     * 
     * @param distribution
     * @param data
     * @return distance
     */
    @Override
    public double countDistance(Distribution distribution, double[] data) {
        double dist = 0.0;
        if (par == 0) {
            for (int i = 0; i < data.length; i++) {  // viz. Radim Demut (dipl. práce) str 37, (6.57)
                dist += Math.log(distribution.getfunctionValue(data[i])); // It's the same for all distributions
            }
            dist = 1 - dist / data.length;
            return dist;
        } else {
            if (distribution.toString().equals("Normal")) {  // viz. Radim Demut (dipl. práce) str 38, (6.78)
                double mu = distribution.getP1();
                double sigma = distribution.getP2();
                for (int i = 0; i < data.length; i++) {
                    dist += Math.exp(-par * Math.pow(data[i] - mu, 2) / (2 * Math.pow(sigma, 2)));
                }
                dist = 1 - dist / data.length * Math.pow(sigma, - par / (1 + par));
                return dist;
            } else if (distribution.toString().equals("Laplace")) {
                double mu = distribution.getP1();
                double theta = distribution.getP2();
                for (int i = 0; i < data.length; i++) {
                    dist += Math.exp(-par * Math.abs(data[i] - mu) / theta);
                }
                dist = 1 - dist / data.length * Math.pow(2*theta,- par / (1 + par));
                return dist;
//            } else if (distribution.toString().equals("Uniform")) {
//                double a = distribution.getP1();
//                double b = distribution.getP2();
//                return 1 - Math.pow(b-a,- par / (1 + par));
            } else if (distribution.toString().equals("Uniform")) {
                double a = distribution.getP1();
                double b = distribution.getP2();
                return Math.pow(b-a,par*par/(1+par)) * 1/(2*Math.sqrt(3*MathUtil.getStandVar(MathUtil.getExpVal(data), data)));
            } else if (distribution.toString().equals("Cauchy")) {
                double mu = distribution.getP1();
                double sigma = distribution.getP2();
                for (int i = 0; i < data.length; i++) {
                    dist +=  Math.pow(1 + Math.pow((data[i]-mu)/sigma,2), -par);
                }
                dist = 1-dist/data.length * Math.pow(sigma, -par/(1+par));
                return dist;
            } else { // viz. Radim Demut (dipl. práce) str 37, (6.57)
                throw new UnsupportedOperationException("Not supported yet.");
                /*
                 * TODO Napsat dalsi rozdeleni. Umime exponencialni, staci dopocitat Weibull
                 */
            }
        }
    }
}
