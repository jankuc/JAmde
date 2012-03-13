/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.estimator.EstimatorBuilder;
import jamde.distribution.Distribution;
import java.util.ArrayList;
import java.util.ArrayList;

/**
 *
 * @author honza
 */
public class TableInput implements Cloneable{

    private Distribution contaminated;
    private Distribution contaminating;
    private double[] data;
    private double contamination;
    private ArrayList<Integer> sizeOfSample = new ArrayList<Integer>();
    private int sizeOfEstimator;
    private ArrayList<EstimatorBuilder> estimators = new ArrayList<EstimatorBuilder>();
    private String paramsCounted; // {"first", "second", "both"}
    private ArrayList<double[]> orderErrors = new ArrayList<double[]>(); // double[]  = double[2], first number is the magnitude of the error, second is its probability
    
    public Distribution getContaminated() {
        return contaminated;
    }

    public Distribution getContaminating() {
        return contaminating;
    }

    public double[] getData() {
        return data;
    }
    
    public double getContamination() {
        return contamination;
    }

    public ArrayList<EstimatorBuilder> getEstimators() {
        return estimators;
    }

    public String getParamsCounted() {
        return paramsCounted;
    }

    public int getSizeOfEstimator() {
        return sizeOfEstimator;
    }

    public ArrayList<Integer> getSizeOfSample() {
        return sizeOfSample;
    }

    public ArrayList<double[]> getOrderErrors() {
        return orderErrors;
    }

    public void setOrderErrors(ArrayList<double[]> orderErrors) {
        this.orderErrors = orderErrors;
    }

    public void setContaminated(Distribution contaminated) {
        this.contaminated = contaminated;
    }

    public void setContaminating(Distribution contaminating) {
        this.contaminating = contaminating;
    }

    public void setContamination(double contamination) {
        this.contamination = contamination;
    }

    public void setData(double[] data) {
        this.data = data;
    }
    
    public void setEstimators(ArrayList<EstimatorBuilder> estimators) {
        this.estimators = estimators;
    }

    public void setParamsCounted(String paramsCounted) {
        this.paramsCounted = paramsCounted;
    }

    public void setSizeOfEstimator(int sizeOfEstimator) {
        this.sizeOfEstimator = sizeOfEstimator;
    }

    public void setSizeOfSample(ArrayList<Integer> sizeOfSample) {
        this.sizeOfSample = sizeOfSample;
    }
    
    @Override
    protected TableInput clone() {
        TableInput input = new TableInput();
        
        input.sizeOfEstimator = this.sizeOfEstimator;
        input.sizeOfSample = this.sizeOfSample;
        input.contaminated = this.contaminated;
        input.contaminating = this.contaminating;
        input.contamination = this.contamination;
        input.paramsCounted = this.paramsCounted;
     
        input.sizeOfSample = this.sizeOfSample;
        input.data = this.data;
        input.estimators = this.estimators;
        input.orderErrors = this.orderErrors;
        return input;
    }
    
}
