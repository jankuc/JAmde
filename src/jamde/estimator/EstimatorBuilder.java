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

    public EstimatorBuilder(String type, double par) {
        this.type = type;
        this.par = par;
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
