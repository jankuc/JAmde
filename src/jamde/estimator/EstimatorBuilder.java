package jamde.estimator;

/**
 * Builder for estimators. Creates estimator based on the type and parameter. 
 *
 * @author  kucerj28@fjfi.cvut.cz
 */
public class EstimatorBuilder {
    /**
     * Type of estimator 
     * types: "Renyi", "LeCam",...
     */
    private String type;
    /**
     * Parameter of estimator
     */
    private double par;
    /**
     * estimator built from type and parameter
     */
    private Estimator estimator;

    
    /**
     * Sets type and parameter of estimator
     * types: "Renyi", "LeCam"
     * 
     * @param type
     * @param par 
     */
    public EstimatorBuilder(String type, double par) {
        setEstimator(type, par);
    }
    
    /**
     * Sets type and parameter of estimator
     * types: "Renyi", "LeCam"
     * 
     * @param type
     * @param par 
     */
    public final void setEstimator(String type, double par) {
        this.par = par;
        this.type = type;
        if (type.equals("Renyi")) estimator = new RenyiEstimator(par);
        if (type.equals("LeCam")) estimator = new LeCamEstimator(par);
    }

    /**
     * 
     * @return created estimator
     */
    public Estimator getEstimator() {
        return estimator;
    }
    
    /**
     * 
     * @return parameter
     */
    public double getPar() {
        return par;
    }

    /**
     * 
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets parameter of estimator
     * 
     * @param par 
     */
    public void setPar(double par) {
        this.par = par;
    }

    /**
     * Sets type of Estimator to type
     * types: "Renyi", "LeCam"
     * 
     * @param type 
     */
    public void setType(String type) {
        this.type = type;
    }
    
}
