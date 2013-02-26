/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;

/**
 *
 * @author honza
/**
 *    tøída LogisticDistribution je potomek tøídy Distribution, má metody :
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


public class LogisticDistribution extends Distribution {
    public static String IDENTIFICATION="Logistic";
    double alfa, beta;

    public LogisticDistribution(double alfa, double beta) {
        this.alfa = alfa;
        this.beta = beta;
        this.upP1 = 5;
        this.lowP1 = -5;
        this.upP2 = 5;
        this.lowP2 = 0;
    }

    @Override
    public double getP1() {
        return alfa;
    }

    @Override
    public double getP2() {
        return beta;
    }

    @Override
    public double getP3() {
        return 0;
    }
    
    @Override
    public void setP1(double p1) {
        this.alfa = p1;
    }

    @Override
    public void setP2(double p2) {
        this.beta = p2;
    }
    
    @Override
    public void setP3(double p3) {
    }
    
    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        double y = beta * Math.log(x / (1 - x)) + alfa;
        return y;
    }

    @Override
    public void setParameters(double p1, double p2, double p3) {
        this.alfa = p1;
        this.beta = p2;
    }

    @Override
    public void setBoundaries(double[] array) {
        this.upP1 = 5;
        this.lowP1 = -5;
        this.upP2 = 20;        //  20
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
//        parameters.p2 = DV * (Math.sqrt(3) / 3.141593);
//        return parameters;
//    }

    @Override
    public double getfunctionValue(double x) {
        double y, z;
        y = -(x - alfa) / beta;
        if (y < 709) {
            if (1 + Math.exp(y) <= Math.pow(10, 154)) {
                z = Math.pow(1 + Math.exp(y), 2);
                y = Math.exp(y) / (z);
            } else y = 0.0;
        } else y = 0.0;
        y = 1 / beta * y;
        return y;
    }

    @Override
    public double getFunctionValue(double x) {
        double y;
        x = -(x - alfa) / beta;
        if (x < 709) y = 1 / (1 + Math.exp(x));
        else y = 0.0;

        return y;
    }
    
    @Override
    public String toString() {
        return "Logistic";
    }
}
