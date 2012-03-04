/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

import jamde.estimator.EstimatorBuilder;
import jamde.distribution.Distribution;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author honza
 */
public class TableInput {

    private Distribution contaminated;
    private Distribution contaminating;
    private double contamination;
    private Collection<Integer> sizeOfSample = new ArrayList<Integer>();
    private int sizeOfEstimator;
    private Collection<EstimatorBuilder> estimators = new ArrayList<EstimatorBuilder>();
    ;
    private String paramsCounted; // {"first", "second", "both"}

    public Distribution getContaminated() {
        return contaminated;
    }

    public Distribution getContaminating() {
        return contaminating;
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

    public void setContaminated(Distribution contaminated) {
        this.contaminated = contaminated;
    }

    public void setContaminating(Distribution contaminating) {
        this.contaminating = contaminating;
    }

    public void setContamination(double contamination) {
        this.contamination = contamination;
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
}
