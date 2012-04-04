/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;

/**
 *    tøída UniformDistribution je potomek tøídy Distribution, má metody :
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


public class UniformDistribution extends Distribution {
    public static String IDENTIFICATION="Uniform";
    double a, b;


    public UniformDistribution(double a, double b) {
        this.a = a;
        this.b = b;
        this.UpB1 = 5;
        this.LowB1 = -15;
        this.UpB2 = 5;
        this.LowB2 = -5;
    }

    @Override
    public double getP1() {
        return a;
    }

    @Override
    public double getP2() {
        return b;
    }

    @Override
    public double getP3() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        double y = a + x * (b - a);
        return y;
    }

    @Override
    public void setParameters(double p1, double p2, double p3) {
        this.a = p1;
        this.b = p2;
    }

    @Override
    public void setBoundaries(double[] array) {
        this.UpB1 = 5;
        this.LowB1 = -5;
        this.UpB2 = 5;
        this.LowB2 = -5;
    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >= LowB1) && (p1 <= UpB1) && (p2 >= LowB2) && (p2 <= UpB2) && (p1 < p2));
    }
//
//    public Parameters getRandomParameters(Distribution d) {
//        Parameters parameters = new Parameters();
//        do {
//            d.setParameters(UpB1, LowB1, 0);
//            parameters.p1 = d.getRealization();
//            d.setParameters(UpB2, LowB2, 0);
//            parameters.p2 = d.getRealization();
//        }
//        while (parameters.p1 >= parameters.p2);
//        return parameters;
//    }
//
//    public Parameters getStandParameters(double[] array, int size) {
//        Parameters parameters = new Parameters();
//        double EV, DV;
//        EV = MathUtil.getExpVal(array, size);
//        DV = MathUtil.getStandDev(EV, array, size, input);
//        parameters.p1 = EV - Math.sqrt(3) * DV;
//        parameters.p2 = EV + Math.sqrt(3) * DV;
//        return parameters;
//    }

    @Override
    public double getfunctionValue(double x) {
        double y;
        if (x <= a) y = 0.0;
        else {
            if (x >= b) y = 0.0;
            else y = 1.0 / (b - a);
        }
        return y;
    }

    @Override
    public double getFunctionValue(double x) {
        double y;
        if (x <= a) y = 0.0;
        else {
            if (x >= b) y = 1.0;
            else y = (x - a) / (b - a);
        }
        return y;
    }
    
    @Override
    public String toString() {
        return "Uniform";
    }
}
