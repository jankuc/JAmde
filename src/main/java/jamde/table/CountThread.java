/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.distribution.*;
import jamde.estimator.*;

/**
 * Thread which encapsulates the procedure for counting.
 * @author kucerj28@fjfi.cvut.cz
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

    /**
     * Public method which only encapsulates the start(); command, which starts the thread and therefore the enumeration.
     * 
     * @return 
     */
    public Distribution[] startCount() {
        start();
        return estimatorArray;
    }
    
    /**
     * Thread is launched sizeOfSample times. For better understanding see 
     * Table.count()
     * 
     */
    @Override
    public void run() {
        //Estimator estimator = estimatorBuilder.getEstimator();
        Distribution contaminated = input.getContaminated();
        for (int i = 0; i < estimatorArray.length; i++) { // cycle counting all the best distributions in estimator Array [1:K]
            Estimator estimator = estimatorBuilder.getEstimator();
            double[] dataArray = Table.createData(input, sizeOfSample);
            // this is where the minimalization begins     

            DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1(), input.getContaminated().getP2(), input.getContaminated().getP3());
            switch (input.getParamsCounted()) {
                case "both":
                    estimatorArray[i] = estimator.estimateFirstAndSecondPar(dB.getDistribution(), dataArray);
                    break;
                case "all":
                    estimatorArray[i] = estimator.estimateAll3Pars(dB.getDistribution(), dataArray);
                    break;
                case "first":
                    estimatorArray[i] = estimator.estimateFirstPar(dB.getDistribution(), dataArray);
                    break;
                case "second":
                    estimatorArray[i] = estimator.estimateSecondPar(dB.getDistribution(), dataArray);
                    break;
                case "third":
                    estimatorArray[i] = estimator.estimateThirdPar(dB.getDistribution(), dataArray);
                    break;
                case "second&third":
                    estimatorArray[i] = estimator.estimateSecondAndThirdPar(dB.getDistribution(), dataArray);
                    break;
            }
        } // END for (i = 0; estimatorArray[i])
    }
}
