/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.estimator.EstimatorBuilder;
import jamde.distribution.Distribution;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author honza
 */
public class TableInput implements Cloneable{

    private Distribution contaminated;
    private Distribution contaminating;
    private double[] data;
    private double contamination;
    private Collection<Integer> sizeOfSample = new ArrayList<Integer>();
    private int sizeOfEstimator;
    private Collection<EstimatorBuilder> estimators = new ArrayList<EstimatorBuilder>();
    private String paramsCounted; // {"first", "second", "both"}
    private Collection<Double[]> orderErrors = new ArrayList<Double[]>();
    
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

    public Collection<EstimatorBuilder> getEstimators() {
        return estimators;
    }

    public String getParamsCounted() {
        return paramsCounted;
    }

    public int getSizeOfEstimator() {
        return sizeOfEstimator;
    }

    public Collection<Integer> getSizeOfSample() {
        return sizeOfSample;
    }

    public Collection<Double[]> getOrderErrors() {
        return orderErrors;
    }

    public void setOrderErrors(Collection<Double[]> orderErrors) {
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
    
    public void setEstimators(Collection<EstimatorBuilder> estimators) {
        this.estimators = estimators;
    }

    public void setParamsCounted(String paramsCounted) {
        this.paramsCounted = paramsCounted;
    }

    public void setSizeOfEstimator(int sizeOfEstimator) {
        this.sizeOfEstimator = sizeOfEstimator;
    }

    public void setSizeOfSample(Collection<Integer> sizeOfSample) {
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
