/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.MathUtil;
import jamde.distribution.Distribution;
import jamde.distribution.UniformDistribution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Estimator is used as a parent for the specific estimators (RenyiEstimator,
 * LeCamEstimator, ...). It is built by EstimatorBuilder. It has abstract
 * function countDistance. Minimization procedures are implemented here.
 *
 * @author kucerj28@fjfi.cvut.cz
 */
public abstract class Estimator {
    /*
     * TODO do estimatoru pridat minimalizovanou ditribuci.
     */
    
    /** Point (p1,p2,p3) characterizing parameters of the distribution.
     * 
     */
    class Point implements Comparable<Point> {
        double p1;
        double p2;
        double p3;
        public double dist = -1;

        public Point(double p1, double p2, double p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        /**
         * Counts distance of the Distribution characterized by the point (p1, p2, p3) to the data.
         * @param distr
         * @param dataArray
         * @return 
         */
        public double countDist(Distribution distr, double[] dataArray) {
            distr.setParameters(p1, p2, p3);
            return this.dist = countDistance(distr, dataArray);
        }
        
        public void setDist (double dist) {
            this.dist = dist;
        }
        
        public double getDist() {
            return dist;
        }
        
        @Override
        public int compareTo(Point x) {
            return (int) Math.signum(this.getDist() - x.getDist());
        }        
        
        @Override
        public String toString() {
            return ("[" + this.p1 + ", " + this.p2 + ", " + this.p3 + ", " + this.dist + "]");
        }
    }

    /**
     * Array of parameters of the estimator
     */
    protected ArrayList<Double> par;

    /**
     * Returns the "whichPar" parameter of the estimator...par[whichPar]
     * 
     * @param whichPar... which parameter of par[] should be returned.
     * @return par[whichPar]
     */
    public double getPar(int whichPar) {
        return par.get(whichPar);
    }
    
    /**
     * Returns First parameter of the estimator... par[0].
     * 
     * @return par[0]
     */
    public double getPar(){
        return getPar(0);
    }

    /**
     * Shouldn't be used, becuase all estimators should be built from
     * EstimatorBuilder
     *
     * Sets Value of First parameter of the estimator...par[0].
     * 
     * @param whichPar...which parameter  of the estimator should be changed
     * @param par...value of the new parameter
     */
    public void setPar(int whichPar,double par) {
        this.par.set(whichPar, par);
    }

    /**
     * Shouldn't be used, becuase all estimators should be built from
     * EstimatorBuilder
     *
     * Sets Value of First parameter of the estimator...par[0].
     * 
     * @param par...new value of the first parameter.
     */
    public void setPar(double par) {
        setPar(0,par);
    }
    
    /**
     * Abstarct method, which is implemented by the estimators with respect to distributions, because the
     * value is strongly dependent on them.
     *
     * @param distr
     * @param data
     * @return
     */
    public abstract double countDistance(Distribution distr, double[] data);

    /**
     * 
     * @return Renyi, a=0.1 
     */
    public abstract String getClassicTableName();
    
    /*
     * TODO V estimateAllPars a ostatnich estimatePars se vysetruje stejna
     * podminka [if (par == 0)] jako v RenyiEstimator.countDistance
     */
    /**
     * Uses one of the optimization procedures to find parameters for the
     * distr's family which minimize the distance from this family's members to
     * the data.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution estimateFirstAndSecondPar(Distribution distr, double[] dataArray) {
//        if (getPar() == ) {  // MLE estimators, or something close to them
//            double EV, DV;
//            if (distr.toString().equals("Cauchy")) {
//                double p = 0.5565;
//                EV = (MathUtil.quantile(dataArray, p) + MathUtil.quantile(dataArray, 1 - p)) / 2;
//                p = 0.75;
//                DV = (MathUtil.quantile(dataArray, p) - MathUtil.quantile(dataArray, 1 - p)) / 2;
//                distr.setParameters(EV, DV, 0);
//            } else if (distr.toString().equals("Laplace")) {
//                EV = MathUtil.quantile(dataArray, 0.5);
//                DV = MathUtil.getLAD(EV, dataArray);
//                distr.setParameters(EV, DV, 0);
//            } else if (distr.toString().equals("Exponential")) {
//                EV = MathUtil.getExpVal(dataArray);
//                DV = MathUtil.min(dataArray);
//                distr.setParameters(DV, EV, 0);
//            //} else if (distr.toString().equals("Weibull")) {
//                
////                double k, m, l;
////                m = distr.getP1();
////                l = distr.getP2();
////                k = distr.getP3();
////                for (int i = 0; i < dataArray.length; i++) {
////                    l += Math.pow(dataArray[i] - m, k);
////                }
////                l = Math.pow(l / dataArray.length, 1 / k);
////                distr.setParameters(m, l, k);
//            } else {
//                EV = MathUtil.getExpVal(dataArray);
//                distr.setParameters(EV, MathUtil.getStandDev(EV, dataArray), distr.getP3());
//            }
//        } else {
            distr = hillClimber2D(distr, dataArray);
//        }
        return distr;
    }

    /**
     * Uses hillClimber3D to find parameters for the Weibulls family which
     * minimize the distance from this family's members to the data.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution estimateAll3Pars(Distribution distr, double[] dataArray) {
        if (getPar() == 0) {  // MLE estimators, or something close to them
            double EV;
            if (distr.toString().equals("Weibull")) {
                double k, m, l;
                m = distr.getP1();
                l = distr.getP2();
                k = distr.getP3();
                for (int i = 0; i < dataArray.length; i++) {
                    l += Math.pow(dataArray[i] - m, k);
                }
                l = Math.pow(l / dataArray.length, 1 / k);
                m = m - 0.005 + Math.random() / 1000;
                k = k - 0.005 + Math.random() / 1000;
                distr.setParameters(k, l, m);
            } else {
                EV = MathUtil.getExpVal(dataArray);
                distr.setParameters(EV, MathUtil.getStandDev(EV, dataArray), distr.getP3());
            }
        } else {
            distr = hillClimber3DX(distr, dataArray);
        }
        return distr;
    }

    /**
     * Uses one of the optimization procedures to find first parameter for the
     * distr's family which minimize the distance from this family's members to
     * the data.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution estimateFirstPar(Distribution distr, double[] dataArray) {
        if (getPar() == 0) {  // MLE estimators, or something close to them
            double estimatedPar;
            if (distr.toString().equals("Cauchy")) {
                double p = 0.5565;
                estimatedPar = (MathUtil.quantile(dataArray, p) + MathUtil.quantile(dataArray, 1 - p)) / 2;
                distr.setParameters(estimatedPar, distr.getP2(), 0);
            } else if (distr.toString().equals("Laplace")) {
                estimatedPar = MathUtil.quantile(dataArray, 0.5);
                distr.setParameters(estimatedPar, distr.getP2(), 0);
            } else if (distr.toString().equals("Exponential")) {
                estimatedPar = MathUtil.min(dataArray);
            } else if (distr.toString().equals("Weibull")) {
                throw new UnsupportedOperationException("Not yet implemented");
            } else {
                estimatedPar = MathUtil.getExpVal(dataArray);
                distr.setParameters(estimatedPar, distr.getP2(), distr.getP3());
            }
        } else {
            distr = hillClimber1DPar1(distr, dataArray);
        }
        return distr;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Uses one of the optimization procedures to find second parameter for the
     * distr's family which minimize the distance from this family's members to
     * the data.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution estimateSecondPar(Distribution distr, double[] dataArray) {
        if (getPar() == 0) { // MLE estimators, or something close to them
            double estimatedPar;
            if (distr.toString().equals("Cauchy")) {
                double p = 0.5565;
                p = 0.75;
                estimatedPar = (MathUtil.quantile(dataArray, p) - MathUtil.quantile(dataArray, 1 - p)) / 2;
                distr.setParameters(distr.getP1(), estimatedPar, 0);
            } else if (distr.toString().equals("Laplace")) {
                estimatedPar = MathUtil.getLAD(distr.getP1(), dataArray);
                distr.setParameters(distr.getP1(), estimatedPar, 0);
            } else if (distr.toString().equals("Exponential")) {
                estimatedPar = MathUtil.getExpVal(dataArray);
                distr.setParameters(distr.getP1(), estimatedPar, 0);
            } else if (distr.toString().equals("Weibull")) {
                double k, m, l;
                m = distr.getP1();
                l = distr.getP2();
                k = distr.getP3();
                for (int i = 0; i < dataArray.length; i++) {
                    l += Math.pow(dataArray[i] - m, k);
                }
                l = Math.pow(l / dataArray.length, 1 / k);
                distr.setParameters(m,l,k);
            } else {
                distr.setParameters(distr.getP1(), MathUtil.getStandDev(distr.getP1(), dataArray), distr.getP3());
            }
        } else {
            distr = hillClimber1DPar2(distr, dataArray);
        }
        return distr;
    }

    /**
     * Uses one of the optimization procedures to find third parameter for the
     * distr's family which minimize the distance from this family's members to
     * the data.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution estimateThirdPar(Distribution distr, double[] dataArray) {
        distr = hillClimber1DPar3(distr, dataArray);
        return distr;
    }

    /**
     * Uses one of the optimization procedures to find first and second
     * parameter for the distr's family which minimize the distance from this
     * family's members to the data.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    public Distribution estimateSecondAndThirdPar(Distribution distr, double[] dataArray) {
        /*
         * TODO minimize second and third pars
         */
        return distr;
    }

    /**
     * Primitive minimization procedure for 2D minimization of first two
     * parameters of distribution. In every iteration moves to one of eight
     * points around actual position so it minimizes the distance the most. If
     * the minimum is in it's current position the epsilon-neighborhood is
     * halved. It ends when the epsilon-neighborhood is smaller or equal to eps
     * = 0.0009. It can't handle local optima.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    private Distribution hillClimber2D(Distribution distr, double[] dataArray) {
        double[] x = new double[9];
        double[] y = new double[9];
        do {
            x[0] = distr.getP1() - 0.5 + Math.random(); // initiation position is near the supposed minimum
            y[0] = distr.getP2() - 0.5 + Math.random(); // initiation position is near the supposed minimum
        } while (!distr.isParametersOK(x[0], y[0], 0));
        double[] distance = new double[9];
        double eps = 0.5;
        int iMin = 0;
        distr.setParameters(x[0], y[0], 0);
        distance[0] = countDistance(distr, dataArray);
        while (eps > 0.0000001) {
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
                if (distr.isParametersOK(x[i], y[i], 0)) {
                    distr.setParameters(x[i], y[i], 0);
                    distance[i] = countDistance(distr, dataArray);
                    if (distance[i] < distance[iMin]) {
                        iMin = i;
                        break;
                    }
                }
            }
            if (iMin == 0) {
                eps *= 0.5;
            } else {
                x[0] = x[iMin];
                y[0] = y[iMin];
                distance[0] = distance[iMin];
                iMin = 0;
            }
        }
        distr.setParameters(x[0], y[0], 0);
        return distr;
    }

    private Distribution hillClimber3D(Distribution distr, double[] dataArray) {
        int numOfPoints = 27;
        double[] x = new double[numOfPoints];
        double[] y = new double[numOfPoints];
        double[] z = new double[numOfPoints];
        do {
            x[0] = distr.getP1() - 0.25 + Math.random()/2; // initiation position is near the supposed minimum
            y[0] = distr.getP2() - 0.25 + Math.random()/2; // initiation position is near the supposed minimum
            z[0] = distr.getP3() - 0.25 + Math.random()/2; // initiation position is near the supposed minimum
        } while (!distr.isParametersOK(x[0], y[0], z[0]));
        double[] distance = new double[numOfPoints];
        double eps = 0.25;
        int iMin = 0;
        distr.setParameters(x[0], y[0], z[0]);
        distance[0] = countDistance(distr, dataArray);
        while (eps > 0.0000001) {
            x[1] = x[0] - eps;
            x[4] = x[1];
            x[6] = x[1];
            x[9] = x[1];
            x[12] = x[1];
            x[15] = x[1];
            x[18] = x[1];
            x[21] = x[1];
            x[24] = x[1];

            x[2] = x[0];
            x[7] = x[2];
            x[10] = x[2];
            x[13] = x[2];
            x[16] = x[2];
            x[19] = x[2];
            x[22] = x[2];
            x[25] = x[2];

            x[3] = x[0] + eps;
            x[5] = x[3];
            x[8] = x[3];
            x[11] = x[3];
            x[14] = x[3];
            x[17] = x[3];
            x[20] = x[3];
            x[23] = x[3];
            x[26] = x[3];

            y[1] = y[0] - eps;
            y[2] = y[1];
            y[3] = y[1];
            y[9] = y[1];
            y[10] = y[1];
            y[11] = y[1];
            y[18] = y[1];
            y[19] = y[1];
            y[20] = y[1];

            y[4] = y[0];
            y[5] = y[0];
            y[12] = y[0];
            y[13] = y[0];
            y[14] = y[0];
            y[21] = y[0];
            y[22] = y[0];
            y[23] = y[0];

            y[6] = y[0] + eps;
            y[7] = y[6];
            y[8] = y[6];
            y[15] = y[6];
            y[16] = y[6];
            y[17] = y[6];
            y[24] = y[6];
            y[25] = y[6];
            y[26] = y[6];

            z[9] = z[0] - eps;
            z[10] = z[9];
            z[11] = z[9];
            z[12] = z[9];
            z[13] = z[9];
            z[14] = z[9];
            z[15] = z[9];
            z[16] = z[9];
            z[17] = z[9];

            z[1] = z[0];
            z[2] = z[0];
            z[3] = z[0];
            z[4] = z[0];
            z[5] = z[0];
            z[6] = z[0];
            z[7] = z[0];
            z[8] = z[0];

            z[18] = z[0] + eps;
            z[19] = z[18];
            z[20] = z[18];
            z[21] = z[18];
            z[22] = z[18];
            z[23] = z[18];
            z[24] = z[18];
            z[25] = z[18];
            z[26] = z[18];

            for (int i = 1; i < distance.length; i++) {
                if (distr.isParametersOK(x[i], y[i], z[i])) {
                    distr.setParameters(x[i], y[i], z[i]);
                    distance[i] = countDistance(distr, dataArray);
                    if (distance[i] < distance[iMin]) {
                        iMin = i;
                        break;
                    }
                }
            }
            if (iMin == 0) {
                eps *= 0.5;
            } else {
                x[0] = x[iMin];
                y[0] = y[iMin];
                z[0] = z[iMin];
                distance[0] = distance[iMin];
                iMin = 0;
            }
        }
        distr.setParameters(x[0], y[0], z[0]);
        return distr;
    }

    private Distribution hillClimber3DX(Distribution distr, double[] dataArray) {
        int numOfPoints = 7;
        double[] x = new double[numOfPoints];
        double[] y = new double[numOfPoints];
        double[] z = new double[numOfPoints];
        do {
            x[0] = distr.getP1() - 0.5 + Math.random(); // initiation position is near the supposed minimum
            y[0] = distr.getP2() - 0.5 + Math.random(); // initiation position is near the supposed minimum
            z[0] = distr.getP3() - 0.5 + Math.random(); // initiation position is near the supposed minimum
        } while (!distr.isParametersOK(x[0], y[0], z[0]));
        double[] distance = new double[numOfPoints];
        double eps = 0.5;
        int iMin = 0;
        distr.setParameters(x[0], y[0], z[0]);
        distance[0] = countDistance(distr, dataArray);
        while (eps > 0.0000001) {
            x[1] = x[0];
            x[2] = x[0];
            x[3] = x[0];
            x[4] = x[0];
            x[5] = x[0] + eps;
            x[6] = x[0] - eps;

            y[3] = y[0];
            y[4] = y[0];
            y[5] = y[0];
            y[6] = y[0];
            y[2] = y[0] + eps;
            y[1] = y[0] - eps;

            z[1] = z[0];
            z[2] = z[0];
            z[5] = z[0];
            z[6] = z[0];
            z[4] = z[0] + eps;
            z[3] = z[0] - eps;

            for (int i = 1; i < distance.length; i++) {
                if (distr.isParametersOK(x[i], y[i], z[i])) {
                    distr.setParameters(x[i], y[i], z[i]);
                    distance[i] = countDistance(distr, dataArray);
                    if (distance[i] < distance[iMin]) {
                        iMin = i;
                        break;
                    }
                }
            }
            if (iMin == 0) {
                eps *= 0.5;
            } else {
                x[0] = x[iMin];
                y[0] = y[iMin];
                z[0] = z[iMin];
                distance[0] = distance[iMin];
                iMin = 0;
            }
        }
        distr.setParameters(x[0], y[0], z[0]);
        return distr;
    }

    /*
     * TODO: minimization procedures are copied everywhere
     */
    /**
     * Primitive minimization procedure for 1D minimization of the first
     * parameter of distribution. In every iteration moves to one of eight
     * points around actual position so it minimizes the distance the most. If
     * the minimum is in it's current position the epsilon-neighborhood is
     * halved. It ends when the epsilon-neighborhood is smaller or equal to eps
     * = 0.0009. It can't handle local optima.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    private Distribution hillClimber1DPar1(Distribution distr, double[] dataArray) {
        double[] x = new double[3];
        double y = distr.getP2();
        do {
            x[0] = distr.getP1() - 0.5 + Math.random(); // initiation position is near the supposed minimum
        } while (!distr.isParametersOK(x[0], y, 0));
        double[] distance = new double[3];
        double eps = 0.5;
        int iMin = 0;
        distr.setParameters(x[0], y, 0);
        distance[0] = countDistance(distr, dataArray);
        while (eps > 0.0000001) {
            x[1] = x[0] - eps;
            x[2] = x[0] + eps;
            for (int i = 1; i < distance.length; i++) {
                if (distr.isParametersOK(x[i], y, 0)) {
                    distr.setParameters(x[i], y, 0);
                    distance[i] = countDistance(distr, dataArray);
                    if (distance[i] < distance[iMin]) {
                        iMin = i;
                        break;
                    }
                }
            }
            if (iMin == 0) {
                eps *= 0.5;
            } else {
                x[0] = x[iMin];
                distance[0] = distance[iMin];
                iMin = 0;
            }
        }
        distr.setParameters(x[0], y, 0);
        return distr;
    }

    /**
     * Primitive minimization procedure for 1D minimization of the second
     * parameter of distribution. In every iteration moves to one of eight
     * points around actual position so it minimizes the distance the most. If
     * the minimum is in it's current position the epsilon-neighborhood is
     * halved. It ends when the epsilon-neighborhood is smaller or equal to eps
     * = 0.0009. It can't handle local optima.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    private Distribution hillClimber1DPar2(Distribution distr, double[] dataArray) {
        double[] y = new double[3];
        double x = distr.getP1();
        do {
            y[0] = distr.getP2() - 0.5 + Math.random(); // initiation position is near the supposed minimum
        } while (!distr.isParametersOK(x, y[0], 0));
        double[] distance = new double[3];
        double eps = 0.5;
        int iMin = 0;
        distr.setParameters(x, y[0], 0);
        distance[0] = countDistance(distr, dataArray);
        while (eps > 0.0000001) {
            y[1] = y[0] - eps;
            y[2] = y[0] + eps;
            for (int i = 1; i < distance.length; i++) {
                if (distr.isParametersOK(x, y[i], 0)) {
                    distr.setParameters(x, y[i], 0);
                    distance[i] = countDistance(distr, dataArray);
                    if (distance[i] < distance[iMin]) {
                        iMin = i;
                        break;
                    }
                }
            }
            if (iMin == 0) {
                eps *= 0.5;
            } else {
                y[0] = y[iMin];
                distance[0] = distance[iMin];
                iMin = 0;
            }
        }
        distr.setParameters(x, y[0], 0);
        return distr;
    }
    
    
    /**
     * Primitive minimization procedure for 1D minimization of the third
     * parameter of distribution. In every iteration moves to one of eight
     * points around actual position so it minimizes the distance the most. If
     * the minimum is in it's current position the epsilon-neighborhood is
     * halved. It ends when the epsilon-neighborhood is smaller or equal to eps
     * = 0.0009. It can't handle local optima.
     *
     * @param distr
     * @param dataArray
     * @return
     */
    private Distribution hillClimber1DPar3(Distribution distr, double[] dataArray) {
        double[] z = new double[3];
        double x = distr.getP1();
        double y = distr.getP2();
        do {
            z[0] = distr.getP3() - 0.5 + Math.random(); // initiation position is near the supposed minimum
        } while (!distr.isParametersOK(x,y, z[0]));
        double[] distance = new double[3];
        double eps = 0.5;
        int iMin = 0;
        distr.setParameters(x,y,z[0]);
        distance[0] = countDistance(distr, dataArray);
        while (eps > 0.0000001) {
            z[1] = z[0] - eps;
            z[2] = z[0] + eps;
            for (int i = 1; i < distance.length; i++) {
                if (distr.isParametersOK(x,y,z[i])) {
                    distr.setParameters(x,y,z[i]);
                    distance[i] = countDistance(distr, dataArray);
                    if (distance[i] < distance[iMin]) {
                        iMin = i;
                        break;
                    }
                }
            }
            if (iMin == 0) {
                eps *= 0.5;
            } else {
                z[0] = z[iMin];
                distance[0] = distance[iMin];
                iMin = 0;
            }
        }
        distr.setParameters(x,y,z[0]);
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
        double y1, y2;
        double x1 = distr.getP1(); // jen nahodna inicializace1
        double x2 = distr.getP2();

        distr.setParameters(x1, x2, 0);
        distOld = countDistance(distr, dataArray);

        double T = 0.1; // Temperature // 0.00000000001
        double lambda = 1; // cooling rate 1
        int i = 1;
        int numOfIterations = 1000;
        double eps = 0.1; // radius of the vicinity of (x1,x2)

        // asi by se to mohlo zmenit tak, aby to nebezelo an pocet iteraci, ale
        // dokud je to daleko, jako v HillClimberu?
        
        while (i < numOfIterations) {
            i++;
            eps *= 0.9999999999; // zmensuje se prohledavane okoli pro (konec=500; eps*=0.99;)
            do {
                y1 = x1 + (rand.getRealization()) * 2 * eps - eps;
            } while ((y1 < distr.lowP1) || (y1 > distr.upP1));
            do {
                y2 = x2 + (rand.getRealization()) * 2 * eps - eps;
            } while ((y2 < distr.lowP2) || (y2 > distr.upP2));
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
    
     
   
    
    /**
     * Minimization procedure via stepwise exhaustive search
     * 
     * upP2
     *  |
     *  |
     *  |  10
     *  |
     *  |
     * lowP2       10 
     * lowP1----------------upP1
     * 
     * @param distr
     * @param dataArray
     * @return 
     */
    private Distribution exhaustiveSearch(Distribution distr, double[] dataArray){
        
        double eps1 = 0.4;
        int numOfMinimsEps1 = 5;
        ArrayList<Point> minimsEps1 = new ArrayList<Point>();
        double[] range = {distr.lowP1, distr.lowP2, distr.upP1, distr.upP2};
        minimsEps1.addAll(Arrays.asList(findMinimsInRange(numOfMinimsEps1, eps1, distr, dataArray, range)));
        
        Collections.sort(minimsEps1);
        //System.out.println(minimsEps1.toString());
        
        double eps2 = 0.02;
        int numOfMinimsEps2 = 5;
        ArrayList<Point> minimsEps2 = new ArrayList<Point>();
        for (Point x : minimsEps1) {
             range[0] = x.p1 - eps1 + eps2;
             range[1] = x.p2 - eps1 + eps2;
             range[2] = x.p1 + eps1 - eps2;
             range[3] = x.p2 + eps1 - eps2;
             minimsEps2.addAll(Arrays.asList(findMinimsInRange(numOfMinimsEps2, eps2, distr, dataArray, range)));
        }
        
        Collections.sort(minimsEps2); // we won't take all the minims, just first 5 (numOfMinimsEps2)
        //System.out.println(minimsEps2.toString());
        
        
        
        double eps3 = 0.001;
        int numOfMinimsEps3 = 5;
        ArrayList<Point> minimsEps3 = new ArrayList<Point>();
        for (Point x : minimsEps2.subList(0, numOfMinimsEps2)) {
             range[0] = x.p1 - eps2 + eps3;
             range[1] = x.p2 - eps2 + eps3;
             range[2] = x.p1 + eps2 - eps3;
             range[3] = x.p2 + eps2 - eps3;
             minimsEps3.addAll(Arrays.asList(findMinimsInRange(numOfMinimsEps3, eps3, distr, dataArray, range)));
        }
        
        Collections.sort(minimsEps3);
//        
//        for (int i = 0; i < minimsEps3.size(); i++) {
//            System.out.println(i + ": " + minimsEps3.get(i).toString());
//        }
        
        distr.setParameters(minimsEps3.get(0).p1, minimsEps3.get(0).p2, minimsEps3.get(0).p3);
        return distr;
    }
    
    /**
     * 
     * @param numOfMinims ... number of minimums, which we want from the
     * @param eps ... size of step ... density of mesh.
     * @param distr .. estimated distribution
     * @param dataArray ... data to which we are counting the distance
     * @param range ... array of ranges: {lowP1, lowP2, lowP3, upP1, upP2, upP3}
     * @return 
     */
    private Point[] findMinimsInRange(int numOfMinims, double eps, Distribution distr, double[] dataArray, double[] range) {
        List<Point> mesh = new ArrayList<Point>();
        
        Point[] minims = new Point[numOfMinims + 1];
        Arrays.fill(minims, new Point(1, 1, 1));
        for (Point x : minims) {
            x.setDist(10);
        }
        
        //We create a mesh of points (we are not counting distance in this loop)
        double p1 = range[0];
        while (p1 <= range[2]) {
            double p2 = range[1];
            while (p2 <= range[3]) {
                mesh.add(new Point(p1, p2, 1));
                p2 = p2 + eps;
            }
            p1 = p1 + eps;
        }
        // We count distances for all points in the mesh
        for (Point x: mesh) {
            x.countDist(distr, dataArray);
            minims[numOfMinims] = x;
            Arrays.sort(minims); // sorts in ascending order
        }
        
        return Arrays.copyOf(minims, numOfMinims);
    }
}
