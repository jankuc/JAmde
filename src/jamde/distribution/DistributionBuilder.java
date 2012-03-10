/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;

/**
 *
 * @author honza
 */
public class DistributionBuilder {
    private Distribution distribution;

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }
    
    public final void setDistribution(String type, double par1, double par2, double par3) {
        if (type.equals("Normal")) distribution = new NormalDistribution(par1, par2);
        if (type.equals("Cauchy")) distribution = new CauchyDistribution(par1, par2);
        if (type.equals("Laplace")) distribution = new LaplaceDistribution(par1, par2);
        if (type.equals("Logistic")) distribution = new LogisticDistribution(par1, par2);
        if (type.equals("Uniform")) distribution = new UniformDistribution(par1, par2);
        if (type.equals("Weibull")) distribution = new WeibullDistribution(par1, par2, par3);
        if (type.equals("Alternative")) distribution = new AlternativeDistribution(par1);
    }
    
    
    public DistributionBuilder(){
        this.setDistribution("Normal", 0, 0, 0);
    }
    
    public DistributionBuilder(String type, double par1, double par2, double par3){
        this.setDistribution(type, par1, par2, par3);
    }
}
