/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

/**
 *
 * @author honza
 */
public class EstimatorBuilder {
    private String type;
    private double par;
    private Estimator estimator;

    public EstimatorBuilder(String type, double par) {
        setEstimator(type, par);
    }
    
    public void setEstimator(String type, double par) {
        this.par = par;
        this.type = type;
        if (type.equals("Renyi")) estimator = new RenyiEstimator(par);
        if (type.equals("LeCam")) estimator = new LeCamEstimator(par);
    }

    public Estimator getEstimator() {
        return estimator;
    }
    
    public double getPar() {
        return par;
    }

    public String getType() {
        return type;
    }

    public void setPar(double par) {
        this.par = par;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
