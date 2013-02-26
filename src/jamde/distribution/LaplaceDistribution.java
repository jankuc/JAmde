/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;

/**
 *
 * @author honza
 */

/**
 *    tøída LaplaceDistribution je potomek tøídy Distribution, má metody :
 *      konstruktor  ... definuje parametry rozdìlení a rozsah parametrického prostoru
 *      getRealization ... dává jednu realizaci náhodné velièiny pøísluného rozdìlení
 *      getP1 ... pøedává 1. parametr rozdìlaní
 *      getP2 ... pøedává 2. parametr rozdìlaní
 *      getP3 ... pøedává 3. parametr rozdìlaní
 *      setBoundaries ... nastavuje hranice parametrického prostoru
 *      setParameters ... nastavuje parametry rozdìlaní
 *      getRandomParameters ... vybere náhodnì jeden bod v parametrickém prostoru
 *      ParametersOK ... testuje, zda zadané parametry patøí do aktuálnì nastaveného parametrického prostoru
 *      getStandParameters ... provádí standardní odhad parametrù rozdìlení
 *      getFunctionValue ... poèítá funkèní hodnotu distribuèní funkce pøísluného rozdìlení v zadaném bodì metodou
 *                           inverzní transformace (kromì pøípadu normálního rozdìlení)
 *      getfunctionValue ... poèítá funkèní hodnotu hustoty pravdìpodobnosti pøísluného rozdìlení v zadaném bodì
 */


public class LaplaceDistribution extends Distribution {
    public static String IDENTIFICATION="Laplace";
    double mu, theta;

    public LaplaceDistribution(double alfa, double beta) {
        this.mu = alfa;
        this.theta = beta;
        this.upP1 = 5;
        this.lowP1 = -5;
        this.upP2 = 5;
        this.lowP2 = 0;
    }

    @Override
    public double getP1() {
        return mu;
    }

    @Override
    public double getP2() {
        return theta;
    }

    @Override
    public double getP3() {
        return 0;
    }
     
    @Override
    public void setP1(double p1) {
        this.mu = p1;
    }

    @Override
    public void setP2(double p2) {
        this.theta = p2;
    }
    
    @Override
    public void setP3(double p3) {
    }

    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        double y;
        if (x > 0.5) y = mu - theta * Math.log(2 - 2 * x);
        else y = mu + theta * Math.log(2 * x);
        return y;
    }

    @Override
    public void setParameters(double p1, double p2, double p3) {
        this.mu = p1;
        this.theta = p2;
    }

    @Override
    public void setBoundaries(double[] array) {
        this.upP1 = 5;
        this.lowP1 = -5;
        this.upP2 = 5;     //20
        this.lowP2 = 0;
    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >= lowP1) && (p1 <= upP1) && (p2 > lowP2) && (p2 <= upP2));
    }
//
//    public Parameters getRandomParameters(Distribution d) {
//        Parameters parameters = new Parameters();
//        d.setParameters(upP1, lowP1, 0);
//        parameters.p1 = d.getRealization();
//        d.setParameters(upP2, lowP2, 0);
//        do
//            parameters.p2 = d.getRealization();
//        while (0 >= parameters.p2);
//        return parameters;
//    }
//
//    public Parameters getStandParameters(double[] array, int size) {
//        double EV, DV;
//        Parameters parameters = new Parameters();
//        EV = MathUtil.getExpVal(array, size);
//        DV = MathUtil.getStandDev(EV, array, size, input);
//        parameters.p1 = EV;
//        parameters.p2 = DV / Math.sqrt(2);
//        return parameters;
//    }

    @Override
    public double getfunctionValue(double x) {
        double y = Math.abs(x - mu) / theta;
        y = Math.exp(-y) / (2 * theta);
        return y;
    }

    @Override
    public double getFunctionValue(double x) {
        double y;
        if (x > mu) y = 1 - (Math.exp(-(x - mu) / theta)) / 2;
        else y = (Math.exp((x - mu) / theta)) / 2;
        return y;
    }
    
    
    @Override
    public String toString() {
        return "Laplace";
    }
}
