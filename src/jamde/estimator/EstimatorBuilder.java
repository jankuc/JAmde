package jamde.estimator;

import java.util.ArrayList;

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
    private String type = "";
    /**
     * Parameter of estimator
     */
    ArrayList<Double> par = new ArrayList<>();
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
    public EstimatorBuilder(String type, ArrayList<Double> par) {
        setEstimator(type, par);
    }
    
    /**
     * Sets type and parameter of estimator
     * types: "Renyi", "LeCam", "Kolmogorov"
     * 
     * @param type
     * @param par 
     */
    public final void setEstimator(String type, ArrayList<Double> par) {
        this.par.clear();
        this.par.addAll(par);
        this.type = type;
        if (type.equals("Renyi")) estimator = new RenyiEstimator(par.get(0));
        if (type.equals("LeCam")) estimator = new LeCamEstimator(par.get(0));
        if (type.equals("Kolmogorov")) estimator = new KolmogorovEstimator();
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
    public ArrayList<Double> getPar() {
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
    public void setPar(ArrayList<Double> par) {
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
