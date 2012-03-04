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
 *      getRandomParameters ... vybere náhodnì jeden bod v parametrickém prostoru
 *      ParametersOK ... testuje, zda zadané parametry patøí do aktuálnì nastaveného parametrického prostoru
 *      getStandParameters ... provádí standardní odhad parametrù rozdìlení
 *      getFunctionValue ... poèítá funkèní hodnotu distribuèní funkce pøísluného rozdìlení v zadaném bodì metodou
 *                           inverzní transformace (kromì pøípadu normálního rozdìlení)
 *      getfunctionValue ... poèítá funkèní hodnotu hustoty pravdìpodobnosti pøísluného rozdìlení v zadaném bodì
 */


public class CauchyDistribution extends Distribution {
    public static String IDENTIFICATION="Cauchy";

    double u, v;
    double PI;


    public CauchyDistribution(double u, double v) {
        this.u = u;
        this.v = v;
        this.UpB1 = 5;
        this.LowB1 = -5;
        this.UpB2 = 5;
        this.LowB2 = 0;
        this.PI = 3.141593;
    }

    @Override
    public double getP1() {
        return u;
    }

    @Override
    public double getP2() {
        return v;
    }

    @Override
    public double getP3() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        double y = v * Math.tan(PI * (x - 0.5)) + u;
        return y;
    }

    @Override
    public void setParameters(double p1, double p2, double p3) {
        this.u = p1;
        this.v = p2;
    }

    @Override
    public void setBoundaries(double[] array) {
        this.UpB1 = 5;
        this.LowB1 = -5;
        this.UpB2 = 20; //20
        this.LowB2 = 0;
    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >= LowB1) && (p1 <= UpB1) && (p2 > LowB2) && (p2 <= UpB2));
    }

//    public Parameters getRandomParameters(Distribution d) {
//        Parameters parameters=new Parameters();
//        d.setParameters(UpB1, LowB1, 0);
//        parameters.p1 = d.getRealization();
//        d.setParameters(UpB2, LowB2, 0);
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
        double y = (x - u) / v;
        y = (1 / (PI * v)) * (1 / (1 + Math.pow(y, 2)));
        return y;
    }

    @Override
    public double getFunctionValue(double x) {
        double y;
        y = 0.5 + (1 / PI) * Math.atan((x - u) / v);
        return y;
    }

    
    @Override
    public String toString() {
        return "Cauchy";
    }
}
