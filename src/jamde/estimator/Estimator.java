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
    /*
     * TODO do estimatoru pridat minimalizovanou ditribuci.
     */
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
            distr1.setParameters(EV,MathUtil.getStandDev(EV, dataArray), 0);
        } else {
            distr1 = searchAndDescent(distr1, dataArray);
        }
        return distr1;
    }

    public Distribution minimalizeFirstPar(Distribution distr, double[] dataArray) {
        /*
         * TODO minimalizace par1
         */
        return distr;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public Distribution minimalizeSeconPar(Distribution distr, double[] dataArray) {
        /*
         * TODO minimalizace par2
         */
        return distr;
    }

    private Distribution searchAndDescent(Distribution distr, double[] dataArray) {
        double[] x = new double[9];
        double[] y = new double[9];
        x[0] = distr.getP1(); // initiation position is near the minimum
        y[0] = distr.getP2();
        double[] distance  = new double[9];
        double eps = 0.5;
        int iMin = 0;
        distance[0] = countDistance(distr, dataArray);
        while (eps > 0.0009) {
            x[1] = x[0] - eps;
            x[7] = x[1];
            x[8] = x[1];
            x[2] = x[0];
            x[6] = x[0];
            x[3] = x[0] + eps;
            x[4] = x[3];
            x[5] = x[3];
            y[1] = y[0] - eps;
            y[2] = y[1];
            y[3] = y[1];
            y[4] = y[0];
            y[8] = y[0];
            y[7] = y[0] + eps;
            y[6] = y[7];
            y[6] = y[7];        
            for (int i = 1; i < distance.length; i++) {
                if (distr.isParametersOK(x[i], y[i],0)){
                    distr.setParameters(x[i], y[i], 0);
                    distance[i] = countDistance(distr, dataArray);
                    if (distance[i] < distance[iMin]) {
                        iMin = i;
                    }
                }
            }
            if (iMin == 0) {
                eps *= 0.5;
            } else {
                x[0] =x[iMin];
                y[0] = y[iMin];
                distance[0] = distance[iMin];
                iMin = 0;
            }
        }
        distr.setParameters(x[iMin], y[iMin], 0);
        return distr;
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
	int numOfIterations = 1000;
        double eps = 0.1; // radius of the vicinity of (x1,x2)

        while (i < numOfIterations) {
            i ++;
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
                distOld = distNew;
                x1 = y1;
                x2 = y2;
            } else {
                if ((rand.getRealization()) < Math.exp(-(distNew - distOld) / T)) {
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