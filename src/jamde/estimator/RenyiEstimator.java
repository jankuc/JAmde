/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.MathUtil;
import jamde.OtherUtils;
import jamde.distribution.Distribution;
import java.util.ArrayList;

/**
 * RenyiEstimator extends Estimator. Implements countDistance().
 * 
 * @author kucerj28@fjfi.cvut.cz
 */
public class RenyiEstimator extends Estimator {

    public RenyiEstimator(double par) {
        super.par = new ArrayList<>();
        super.par.add(par);
    }
    
    /* 
     * TODO V estimateAllPars a ostatnich estimatePars se vysetruje stejna podminka [if (par == 0)] jako v RenyiEstimator.countDistance
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
        double alpha = getPar(); // parameter of the Renyi estimator.
        if (alpha == 0) {
            // TODO je mozne toto udelat pro kazde rozdeleni zvlast a u nekterych tim snizit vypocetni cas.
            for (int i = 0; i < data.length; i++) {  // viz. Radim Demut (dipl. práce) str 37, (6.57)
                dist +=  Math.log(distribution.getfunctionValue(data[i]))/data.length; // It's the same for all distributions. There should be dist += log(...), but it doesn't matter for the minimization and it's faster
            }
            dist = 1 - (dist);
            return dist;
        } else {
            if (distribution.toString().equals("Normal")) {  // viz. Radim Demut (dipl. práce) str 38, (6.78)
                double mu = distribution.getP1();
                double sigma = distribution.getP2();
                for (int i = 0; i < data.length; i++) {
                    dist += Math.exp(-alpha * Math.pow(data[i] - mu, 2) / (2 * Math.pow(sigma, 2)));
                }
                dist = 1 - dist / data.length * Math.pow(sigma, - alpha / (1 + alpha));
                return dist;
            } else if (distribution.toString().equals("Laplace")) {
                double mu = distribution.getP1();
                double theta = distribution.getP2();
                for (int i = 0; i < data.length; i++) {
                    dist += Math.exp(-alpha * Math.abs(data[i] - mu) / theta);
                }
                dist = 1 - dist / data.length * Math.pow(2*theta,- alpha / (1 + alpha));
                return dist;
            } else if (distribution.toString().equals("Uniform")) {
                double a = distribution.getP1();
                double b = distribution.getP2();
                return 1 - Math.pow(b-a,alpha*alpha/(1+alpha)) * 1/(2*Math.sqrt(3*MathUtil.getStandVar(MathUtil.getExpVal(data), data)));
            } else if (distribution.toString().equals("Cauchy")) {
                double mu = distribution.getP1();
                double sigma = distribution.getP2();
                for (int i = 0; i < data.length; i++) {
                    dist +=  Math.pow(1 + Math.pow((data[i]-mu)/sigma,2), -alpha);
                }
                dist = 1-dist/data.length * Math.pow(sigma, -alpha/(1+alpha));
                return dist;
            } else if (distribution.toString().equals("Weibull")) {
                double m = distribution.getP1();
                double l = distribution.getP2();
                double k = distribution.getP3();
                double z;
                for (int i = 0; i < data.length; i++) {
                    z = (data[i]-m)/l;
                    if(z>=0){
                      dist +=  Math.pow(z,alpha*(k-1))*Math.exp(-alpha*Math.pow(z,k));
                    }
                }
                z = (k+alpha*k-alpha)/k;
                double y = alpha/(alpha+1);
                dist *= Math.pow(k/l,y)*Math.pow(1+alpha,z*y)*Math.pow(MathUtil.gamma(z),-y);
                dist = 1-dist/data.length;
                return dist;
             } else if (distribution.toString().equals("Exponential")) {
                double m = distribution.getP1();
                double l = distribution.getP2();
                double z;
                for (int i = 0; i < data.length; i++) {
                  z = (data[i]-m)/l;
                  if (z>=0) {
                    dist +=  Math.exp(-alpha*z);
                  } 
                }
                dist = 1 - Math.pow(l,-alpha/(1+alpha)) * dist/data.length;
                return dist;
            } else { // viz. Radim Demut (dipl. práce) str 37, (6.57)
                throw new UnsupportedOperationException("Not supported yet.");
                /*
                 * TODO Napsat dalsi rozdeleni. Umime exponencialni, staci dopocitat Weibull
                 */
            }
        }
    }
    
    @Override
    public String toString(){
        return ("Renyi");
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{Renyi}, \\alpha="+OtherUtils.num2str(getPar()) + "$");
    }
}