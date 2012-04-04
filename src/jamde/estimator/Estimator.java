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
 * Estimator is used as a parent for the specific estimators (RenyiEstimator, LeCamEstimator, ...). It is built by EstimatorBuilder. 
 * It has abstract function countDistance.
 * Minimization procedures are implemented here.
 * 
 * @author honza
 */
public abstract class Estimator {
    /*
     * TODO do estimatoru pridat minimalizovanou ditribuci.
     */
    
    /**
     * Parameter of the estimator
     */
    protected double par;

    /**
     * Returns the parameter of the estimator.
     * 
     * @return  par
     */
    public double getPar() {
        return par;
    }

    /**
     * Shouldn't be used, becuase all estimators should be built from EstimatorBuilder
     * 
     * @param par
     */
    public void setPar(double par) {
        this.par = par;
    }
    
    /**
     * Abstarct method, which is implemented by the distributions, because the value is strongly dependent on them.
     * 
     * @param distr
     * @param data
     * @return
     */
    public abstract double countDistance(Distribution distr, double[] data);

    /**
     * Uses one of the optimization procedures to find parameters for the distr1's family which minimize the distance from this family's members to the data.
     * 
     * @param distr1
     * @param dataArray
     * @return
     */
    public Distribution minimalize(Distribution distr1, double[] dataArray) {
        if (par == 0) {
            double EV = MathUtil.getExpVal(dataArray);
            distr1.setParameters(EV,MathUtil.getStandDev(EV, dataArray), 0);
        } else {
            distr1 = simulatedAnnealing(distr1, dataArray);
        }
        return distr1;
    }

    /**
     * Uses one of the optimization procedures to find first parameter for the distr1's family which minimize the distance from this family's members to the data.
     * 
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution minimalizeFirstPar(Distribution distr, double[] dataArray) {
        /*
         * TODO minimalizace par1
         */
        return distr;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Uses one of the optimization procedures to find second parameter for the distr1's family which minimize the distance from this family's members to the data.
     * 
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution minimalizeSeconPar(Distribution distr, double[] dataArray) {
        /*
         * TODO minimalizace par2
         */
        return distr;
    }

    /**
     * Primitive minimization procedure for 2D minimization. In every iteration 
     * moves to one of eight points around actual position so it minimizes 
     * the distance the most. If the minimum is in it's current position the 
     * epsilon-neighborhood is halved. It ends when the epsilon-neighborhood is
     * smaller or equal to eps = 0.0009.
     * It can't handle local optima. 
     * 
     * 
     * @param distr
     * @param dataArray
     * @return 
     */
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
    
    
    /**
     * Minimization procedure for 2D minimization. Moves to the random point in 
     * the epsilon-neighborhood of actual position and according to distance and
     * temperature jumps there or not. It ends after numOfIterations iterations.
     * After setting the right parameters it should handle jumping out of local 
     * optima.
     * 
     * @param distr
     * @param dataArray
     * @return 
     */
    private Distribution simulatedAnnealing(Distribution distr, double[] dataArray) {
        double distOld, distNew;
        Distribution rand = new UniformDistribution(0, 1);
        double y1,y2;
        double x1 = distr.getP1(); // jen nahodna inicializace
        double x2 = distr.getP2();
        
        distr.setParameters(x1, x2, 0);
        distOld = countDistance(distr, dataArray);
        
        double T = 0.1; // Temperature // 0.00000000001
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