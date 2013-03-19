package jamde.table;

import jamde.distribution.Distribution;
import jamde.estimator.EstimatorBuilder;
import java.util.ArrayList;

/**
 * Class which is used for storing input parameters. It is almost only for use 
 * as an inner class for Table.
 * 
 * @author kucerj28@fjfi.cvut.cz
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
    
    /**
     * 
     * @return contaminated distribution
     */
    public Distribution getContaminated() {
        return contaminated;
    }

    /**
     * 
     * @return contaminating distribution
     */
    public Distribution getContaminating() {
        return contaminating;
    }

    /**
     * 
     * @return data
     */
    public double[] getData() {
        return data;
    }
    
    /**
     * 
     * @return contamination
     */
    public double getContamination() {
        return contamination;
    }

    /**
     * 
     * @return estimatorBuilders
     */
    public ArrayList<EstimatorBuilder> getEstimators() {
        return estimators;
    }

    /**
     * 
     * @return "both" or "first" or "second" or "third" or "all" or "second&third"
     */
    public String getParamsCounted() {
        return paramsCounted;
    }

    /**
     * In the tables usually K = 1000
     * 
     * @return sizeOfEstimator
     */
    public int getSizeOfEstimator() {
        return sizeOfEstimator;
    }

    /**
     * In the tables usually n = {20, 50, 100, 200, 500}
     * 
     * @return sizeOfSamples
     */
    public ArrayList<Integer> getSizeOfSample() {
        return sizeOfSample;
    }

    /**
     * 
     * @return orderErrors
     */
    public ArrayList<double[]> getOrderErrors() {
        return orderErrors;
    }

    /**
     * 
     * @param orderErrors
     */
    public void setOrderErrors(ArrayList<double[]> orderErrors) {
        this.orderErrors = orderErrors;
    }

    /**
     * 
     * @param contaminated distribution
     */
    public void setContaminated(Distribution contaminated) {
        this.contaminated = contaminated;
    }

    /**
     * 
     * @param contaminating distribution
     */
    public void setContaminating(Distribution contaminating) {
        this.contaminating = contaminating;
    }

    /**
     * 
     * @param contamination
     */
    public void setContamination(double contamination) {
        this.contamination = contamination;
    }

    /**
     * 
     * @param data[]
     */
    public void setData(double[] data) {
        this.data = data;
    }
    
    /**
     * 
     * @param estimatorBuilders
     */
    public void setEstimators(ArrayList<EstimatorBuilder> estimators) {
        this.estimators = estimators;
    }

    /**
     * 
     * @param paramsCounted in {"both", "first", "second"}
     */
    public void setParamsCounted(String paramsCounted) {
        this.paramsCounted = paramsCounted;
    }

    /**
     * In the tables usually K = 1000
     * 
     * @param sizeOfEstimator
     */
    public void setSizeOfEstimator(int sizeOfEstimator) {
        this.sizeOfEstimator = sizeOfEstimator;
    }

    /**
     * In the tables usually n = {20, 50, 100, 200, 500}
     * 
     * @param sizeOfSamples
     */
    public void setSizeOfSample(ArrayList<Integer> sizeOfSample) {
        this.sizeOfSample = sizeOfSample;
    }
    
    /**
     * 
     * @return clone of this tableInput
     */
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
    
    public String getDistributionsString() {
        String sContaminating = String.format("%c%.0f,%.0f", getContaminating().toString().charAt(0), getContaminating().getP1(), getContaminating().getP2());
        String sContaminated= String.format("%c%.0f,%.0f", getContaminated().toString().charAt(0), getContaminated().getP1(), getContaminated().getP2());
        
        return (sContaminated + "-" + sContaminating);
        
    }
}
