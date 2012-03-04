/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;


/**
 *    tøída WeibullDistribution je potomek tøídy Distribution, má metody :
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


public class WeibullDistribution extends Distribution {
    public static String IDENTIFICATION="Weibull";
    double m, c, b;


    public WeibullDistribution(double m, double c, double b) {
        this.m = m;
        this.c = c;
        this.b = b;
        this.UpB1 = 5;
        this.LowB1 = -5;
        this.UpB2 = 5;
        this.LowB2 = 0;
        this.UpB3 = 5;
        this.LowB3 = 0.1;
    }

    @Override
    public double getP1() {
        return m;
    }

    @Override
    public double getP2() {
        return c;
    }

    @Override
    public double getP3() {
        return b;
    }

    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        double y = m + c * Math.pow(Math.log(1 / (1 - x)), 1 / b);
        return y;
    }

    @Override
    public void setParameters(double p1, double p2, double p3) {
        this.m = p1;
        this.c = p2;
        this.b = p3;
    }

    @Override
    public void setBoundaries(double[] array) {
        this.UpB1 = 5;
        this.LowB1 = -5;
        this.UpB2 = 5;
        this.LowB2 = 0;
        this.UpB3 = 3;
        this.LowB3 = 1.0;//   0.1;
    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >= LowB1) && (p1 <= UpB1) && (p2 > LowB2) && (p2 <= UpB2) && (p3 >= LowB3) && (p3 <= UpB3));
    }
//
//    public Parameters getRandomParameters(Distribution d) {
//        Parameters parameters = new Parameters();
//        d.setParameters(UpB1, LowB1, 0);
//        parameters.p1 = d.getRealization();
//        d.setParameters(UpB2, LowB2, 0);
//        do
//            parameters.p2 = d.getRealization();
//        while (0 >= parameters.p2);
//        d.setParameters(UpB3, LowB3, 0);
//        do
//            parameters.p3 = d.getRealization();
//        while (0 >= parameters.p3);
//        return parameters;
//    }
//
//    public Parameters getStandParameters(double[] array, int size) {
//        double m, b0, b, c, t1, t2, W, sumA, sumB, sumC, sumD, z1, z2, dif;
//        Parameters parameters = new Parameters();
//        int index1, index2;
//
//        index1 = (int) Math.floor(size * 0.1673);
//        index2 = (int) Math.floor(size * 0.9736);
//        t1 = array[index1];
//        t2 = array[index2];
//        W = Math.log(Math.log(1 - 0.9736) / Math.log(1 - 0.1673));
//        b0 = W / Math.log((t2 - array[0]) / (t1 - array[0]));
//
//        m = (array[0] * array[size - 1] - array[1] * array[1]) /
//                (array[0] + array[size - 1] - 2 * array[1]);
//        if (m == array[0]) m = m - 0.0000001;
//        b = b0;
//        do {
//            sumA = 0;
//            sumB = 0;
//            sumC = 0;
//            sumD = 0;
//            dif = b;
//            for (int i = 0; i < size; i++) {
//                z1 = Math.pow(array[i] - m, b);
//                z2 = Math.log(array[i] - m);
//                sumA += z1;
//                sumB += z1 * z2;
//                sumC += z2;
//                sumD += z1 * z2 * z2;
//            }
//            b = b + (size * b * sumA * sumA - size * b * b * sumA * sumB + b * b * sumA * sumA * sumC) /
//                    (size * sumA * sumA + size * b * b * (sumD * sumA - sumB * sumB));
//            dif = dif - b;
//        }
//        while ((b >= 0) && (dif > 0.001));
//
//        sumA = 0;
//        for (int i = 0; i < size; i++) {
//            sumA += Math.pow(array[i] - m, b);
//        }
//        c = Math.pow(sumA / size, 1 / b);
//
//        parameters.p1 = m;
//        parameters.p2 = c;
//        parameters.p3 = b;
//        return parameters;
//    }

    @Override
    public double getfunctionValue(double x) {
        double y;
        if (x > m) {
            y = (x - m) / c;
            y = (b / c) * Math.pow(y, b - 1) * Math.exp(-Math.pow(y, b));
        } else y = 0.0;
        return y;
    }

    @Override
    public double getFunctionValue(double x) {
        double y;
        if (x > m) {
            y = (x - m) / c;
            y = Math.pow(y, b);
            y = 1 - Math.exp(-y);
        } else y = 0.0;
        return y;
    }
    
    
    @Override
    public String toString() {
        return "Weibull";
    }
}
