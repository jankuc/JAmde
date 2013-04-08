/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;

/**
 *    tøída CauchyDistribution je potomek tøídy Distribution, má metody :
 *      konstruktor  ... definuje parametry rozdìlení a rozsah parametrického prostoru
 *      getRealization ... dává jednu realizaci náhodné velièiny pøísluného rozdìlení
 *      getP1 ... pøedává 1. parametr rozdìlaní
 *      getP2 ... pøedává 2. parametr rozdìlaní
 *      getP3 ... pøedává 3. parametr rozdìlaní
 *      setBoundaries ... nastavuje hranice parametrického prostoru
 *      setParameters ... nastavuje parametry rozdìlaní
 *      getRandomParameters ... vybere náhodnì jeden bod sigma parametrickém prostoru
 *      ParametersOK ... testuje, zda zadané parametry patøí do aktuálnì nastaveného parametrického prostoru
 *      getStandParameters ... provádí standardní odhad parametrù rozdìlení
 *      getFunctionValue ... poèítá funkèní hodnotu distribuèní funkce pøísluného rozdìlení sigma zadaném bodì metodou
 *                           inverzní transformace (kromì pøípadu normálního rozdìlení)
 *      getfunctionValue ... poèítá funkèní hodnotu hustoty pravdìpodobnosti pøísluného rozdìlení sigma zadaném bodì
 */


public class CauchyDistribution extends Distribution {
    public static String IDENTIFICATION="Cauchy";

    double mu, sigma;
    double PI;


    public CauchyDistribution(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
        this.upP1 = 10;
        this.lowP1 = -10;
        this.upP2 = 20;
        this.lowP2 = 0;
        this.PI = 3.141593;
    }

    @Override
    public double getP1() {
        return mu;
    }

    @Override
    public double getP2() {
        return sigma;
    }

    @Override
    public double getP3() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    
    @Override
    public void setP1(double p1) {
        this.mu = p1;
    }

    @Override
    public void setP2(double p2) {
        this.sigma = p2;
    }
    
    @Override
    public void setP3(double p3) {
    }

    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        double y = sigma * Math.tan(PI * (x - 0.5)) + mu;
        return y;
    }

    @Override
    public void setParameters(double p1, double p2, double p3) {
        this.mu = p1;
        this.sigma = p2;
    }

    @Override
    public void setBoundaries(double[] array) {
        this.upP1 = 5;
        this.lowP1 = -5;
        this.upP2 = 20; //20
        this.lowP2 = 0;
    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >= lowP1) && (p1 <= upP1) && (p2 > lowP2) && (p2 <= upP2));
    }

//    public Parameters getRandomParameters(Distribution d) {
//        Parameters parameters=new Parameters();
//        d.setParameters(upP1, lowP1, 0);
//        parameters.p1 = d.getRealization();
//        d.setParameters(upP2, lowP2, 0);
//        do
//        parameters.p2 = d.getRealization();
//        while (0 >=parameters.p2);
//        return parameters;
//    }
//
//    public Parameters getStandParameters(double[] array, int size) {
//        Parameters parameters=new Parameters();
//        int index = (int)Math.round(size * 0.5);
//        parameters.p1 = array[index];
//        index = (int)Math.round(size * 0.25);
//        double b = array[index];
//        index = (int)Math.round(size * 0.75);
//        double c = array[index];
//        parameters.p2 = (c - b) / 2;
//        return parameters;
//    }

    @Override
    public double getfunctionValue(double x) {
        double y = (x - mu) / sigma;
        return (1 / (Math.PI * sigma)) * (1 / (1 + Math.pow(y, 2)));
    }

    @Override
    public double getFunctionValue(double x) {
        double y;
        y = 0.5 + (1 / PI) * Math.atan((x - mu) / sigma);
        return y;
    }

    
    @Override
    public String toString() {
        return "Cauchy";
    }
}
