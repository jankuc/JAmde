/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.distribution.*;
import jamde.estimator.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author honza
 */
class CountThread extends Thread {

    private TableInput input;
    private int load;
    private Distribution[] estimatorArray;
    private EstimatorBuilder estimatorBuilder;
    private int sizeOfSample;

    public CountThread(TableInput input, int load, EstimatorBuilder estimatorBuilder, int sizeOfSample) {
        this.input = input;
        this.load = load;
        estimatorArray = new Distribution[load];
        this.estimatorBuilder = estimatorBuilder;
        this.sizeOfSample = sizeOfSample;
    }

    public Distribution[] startCount() {
        /*
         * TODO prekopirovat sem veci a zaridit aby to fungovalo
         */
        start();
        return estimatorArray;
    }

    @Override
    public void run() {
        Estimator estimator = estimatorBuilder.getEstimator();
        Distribution contaminated = input.getContaminated();

        for (int i = 0; i < estimatorArray.length; i++) { // cycle counting all the best distributions in estimator Array [1:K]
            // DATA creation
            double[] dataArray = new double[sizeOfSample];
            if (input.getContaminating() == null) { // if true then it's not mixture of distributions
                if (input.getOrderErrors() == null) { // if true then data was loaded from file
                    System.arraycopy(input.getData(), 0, dataArray, 0, dataArray.length);
                } else { // data will be created as a distribution with errors in order
                    Distribution alt = new AlternativeDistribution(input.getContamination());
                    /*
                     * TODO vytvoreni dat s ustrely o rad
                     */
                }
            } else { // data will be created as mixture of two distributions
                Distribution contaminating = input.getContaminating();
                Distribution alternative = new AlternativeDistribution(input.getContamination());
                for (int j = 0; j < dataArray.length; j++) {
                    if (alternative.getRealization() < 0.5) { // if alternative = 0.0 <=> we do not contaminate (we do not use equality because of rounding errors)
                        dataArray[j] = contaminated.getRealization();
                    } else {
                        dataArray[j] = contaminating.getRealization();
                    }
                }
            } // Here the dataArray is completely filled with data
            //printDataArray:
//                        for(double k : dataArray){
//                            System.out.println(k);
//                        }
//                        
            if (false) {
                PrintWriter w;
                try {
                    w = new PrintWriter("./distances");

                    double mu;
                    double sigma2;
                    double dist;
                    Distribution d = new NormalDistribution(0, 1);
                    double N = 200;
                    double delkaIntervalu = 1;
                    for (int j = 0; j < N; j++) {
                        mu = -0.5 * delkaIntervalu + j * delkaIntervalu / N;
                        for (int k = 0; k < N; k++) {
                            sigma2 = 0.0001 + k * 2 * delkaIntervalu / N;
                            d.setParameters(mu, sigma2, 0);
                            dist = estimator.countDistance(d, dataArray);
                            w.format(" %.6f ", dist);
                        }
                        w.format(" \n ");
                    }
                    w.format(" \n ");
                    w.close();
                    System.out.println("Vypsani Distance souboru skonceno.");
                    return;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CountThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // this is where the minimalization begins     
            if (input.getParamsCounted().equals("both")) {
                DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1(), input.getContaminated().getP2(), input.getContaminated().getP3());
                estimatorArray[i] = estimator.minimalize(dB.getDistribution(), dataArray);
            } else if (input.getParamsCounted().equals("first")) {
                /*
                 * TODO dodelat minimalizaci pro jeden parametr
                 * v tuto chvili vraci jen contaminated distribution
                 */
                estimatorArray[i] = estimator.minimalizeFirstPar(estimatorArray[i], dataArray);
            } else {
                estimatorArray[i] = estimator.minimalizeSeconPar(estimatorArray[i], dataArray);
            }
        } // END for (i = 0; estimatorArray[i] : estimatorArray)
    }
}
