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
     * Exasmples:
     * Renyi 0.3
     * Kolmogorov
     * CramerRationalPower 34 19
     * CramerRationalPower 10 19
     * KolmCram 10 19 20
     * KolmCramBeta 8 5 0.3
     * KolmCramRand 1 5 20 0.3 0.7
     * KolmCramFRand 34 19 0.4 0.5 0.6
     * KolmCramBetaRand 10 19 0.4 0.5 0.6
     * 
     * 
     * @param type
     * @param par 
     */
    public final void setEstimator(String type, ArrayList<Double> par) {
        this.par.clear();
        this.par.addAll(par);
        this.type = type;
        if (type.equals("Renyi")) { 
            estimator = new RenyiEstimator(par.get(0));
        } 
        if (type.equals("LeCam")) {
            estimator = new LeCamEstimator(par.get(0));
        }
        if (type.equals("Kolmogorov")) {
            estimator = new KolmogorovEstimator();
        }
        if (type.equals("CramGen")) {
            estimator = new CramGenEstimator((int) Math.round(par.get(0)), (int) Math.round(par.get(1)));
        }
        if (type.equals("KolmCramM")) {
            estimator = new KolmCramMEstimator((int) Math.round(par.get(0)), (int) Math.round(par.get(1)), (int) Math.round(par.get(2)));
        }
        if (type.equals("KolmCramBeta")) {
            estimator = new KolmCramBetaEstimator((int) Math.round(par.get(0)), (int) Math.round(par.get(1)), par.get(2));
        }
        if (type.equals("KolmCramRandM")) { 
            estimator = new KolmCramRandMEstimator((int) Math.round(par.get(0)), (int) Math.round(par.get(1)), (int) Math.round(par.get(2)), par.get(3), par.get(4));
        }
        if (type.equals("KolmCramRandF")) { 
            estimator = new KolmCramRandFEstimator((int) Math.round(par.get(0)), (int) Math.round(par.get(1)), par.get(2), par.get(3), par.get(4));
        }
        if (type.equals("KolmCramRandBeta")) { 
            estimator = new KolmCramRandBetaEstimator((int) Math.round(par.get(0)), (int) Math.round(par.get(1)), par.get(2), par.get(3), par.get(4));
        }
        if (type.equals("Power")) { // should be the same as PowerH
        	estimator = new PowerEstimator(par.get(0));
        }
        if (type.equals("PowerH")) {
        	estimator = new PowerEstimator(par.get(0), PowerEstimator.DensityType.HISTOGRAM);
        }
        if (type.equals("PowerHS")) {
        	estimator = new PowerEstimator(par.get(0), PowerEstimator.DensityType.HISTOGRAM_STOCH);
        }
        if (type.equals("PowerK")) {
        	estimator = new PowerEstimator(par.get(0), PowerEstimator.DensityType.KERNEL);
        }
        if (type.equals("PowerKT")) {
        	estimator = new PowerEstimator(par.get(0), PowerEstimator.DensityType.KERNEL_TRIAG);
        }
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
