/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.distribution.*;
import jamde.estimator.*;

/**
 * 
 * @author kucerj28
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
        Estimator estimator = estimatorBuilder.getEstimator();
        Distribution contaminated = input.getContaminated();
        for (int i = 0; i < estimatorArray.length; i++) { // cycle counting all the best distributions in estimator Array [1:K]
            double[] dataArray = Table.createData(input, sizeOfSample);
            // this is where the minimalization begins     
            if (input.getParamsCounted().equals("both")) {
                DistributionBuilder dB = new DistributionBuilder(input.getContaminated().toString(), input.getContaminated().getP1(), input.getContaminated().getP2(), input.getContaminated().getP3());
                estimatorArray[i] = estimator.minimalize(dB.getDistribution(), dataArray);
            } else if (input.getParamsCounted().equals("first")) {
                estimatorArray[i] = estimator.minimalizeFirstPar(estimatorArray[i], dataArray);
            } else {
                estimatorArray[i] = estimator.minimalizeSeconPar(estimatorArray[i], dataArray);
            }
        } // END for (i = 0; estimatorArray[i])
    }

    
}
