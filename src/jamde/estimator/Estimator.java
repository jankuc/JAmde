/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.MathUtil;
import jamde.distribution.Distribution;
import jamde.distribution.DistributionBuilder;
import jamde.distribution.UniformDistribution;

/**
 *
 * @author honza
 */
public abstract class Estimator {
 
    protected double par;

    public double getPar() {
        return par;
    }

    public void setPar(double par) {
        this.par = par;
    }
    
    public abstract double countDistance(Distribution distr, double[] data);

    public Distribution minimalize(Distribution distr1, double[] dataArray) {
        /*
         * TODO minimalizace
         */
        //DistributionBuilder dB = new DistributionBuilder(distr.toString(),distr.getP1(),  distr.getP2(),distr.getP3());
        //Distribution distr1 = dB.getDistribution();

        if (par == 0) {
            double EV = MathUtil.getExpVal(dataArray);
            
            distr1.setParameters(EV,MathUtil.getStandVar(EV, dataArray), 0);
        } else {
            distr1 = simulatedAnnealing(distr1, dataArray);
        }
        return distr1;
    }

    public Distribution minimalizeFirstPar(Distribution closestDistribution, double[] dataArray) {
        /*
         * TODO minimalizace par1
         */
        return closestDistribution;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public Distribution minimalizeSeconPar(Distribution closestDistribution, double[] dataArray) {
        /*
         * TODO minimalizace par2
         */
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Distribution simulatedAnnealing(Distribution distr, double[] dataArray) {
        double distOld, distNew;
        Distribution rand = new UniformDistribution(0, 1);
        double y1,y2;
        double x1 = distr.getP1(); // jen nahodna inicializace
        double x2 = distr.getP2();
        
        distr.setParameters(x1, x2, 0);
        distOld = countDistance(distr, dataArray);
        
        double T = 0.000001; // Temperature // 0.00000000001
	double lambda = 1; // cooling rate 1
	int i = 1;
	int numOfIterations = 8;
        double eps = 0.1; // radius of the vicinity of (x1,x2)

        while (i < numOfIterations) {
            eps *= 0.9999999999; // zmensuje se prohledavane okoli pro (konec=500; eps*=0.99;)
            do {
                y1 = x1 + (rand.getRealization()) * 2 * eps - eps;
            } while ((y1 < distr.LowB1) || (y1 > distr.UpB1));
            do {
                y2 = x2 + (rand.getRealization()) * 2 * eps - eps;
            } while ((y2 < distr.LowB2) || (y2 > distr.UpB2));
            distr.setParameters(y1, y2, 0);
            distNew = countDistance(distr, dataArray);
            if (distOld > distNew) {
                i = i + 1;
                distOld = distNew;
                x1 = y1;
                x2 = y2;
            } else {
                if ((rand.getRealization()) < Math.exp(-(distNew - distOld) / T)) {
                    i = i + 1;
                    distOld = distNew;
                    x1 = y1;
                    x2 = y2;
                }
            }
            T = lambda * T;
        }
        return distr;
    }
}