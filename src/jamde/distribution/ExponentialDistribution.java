/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;


/**
 *    tøída ExponentialDistribution je potomek tøídy Distribution, má metody :
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



/**
 *   f = 1/l * Exp[ - [(x-m)/l] ] <br>
 * 
 * @author kucerj28@fjfi.cvut.cz
 */
public class ExponentialDistribution extends Distribution {
    public static String IDENTIFICATION="Weibull";
    double m, l, k = 1;

    /**
     *   f = 1/l * Exp[ - [(x-m)/l] ] <br>
     *   m...p1...parameter of position
     *   l...p2...parameter of scale
     * @author honza
    */
    public ExponentialDistribution(double p1, double p2) {
        this.m = p1; // parameter of location
        this.l = p2; // parameter of scale
        this.upP1 = 10;
        this.lowP1 = -10;
        this.upP2 = 20;
        this.lowP2 = 0.001;
        this.upP3 = 0;
        this.lowP3 = 0;
    }

    @Override
    public double getP1() {
        return m;
    }

    @Override
    public double getP2() {
        return l;
    }

    @Override
    public double getP3() {
        return 0;
    }
    
    @Override
    public void setP1(double p1) {
        this.m = p1;
    }

    @Override
    public void setP2(double p2) {
        this.l = p2;
    }
    
    @Override
    public void setP3(double p3) {
    }
    
    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        double y = m + l * Math.pow(Math.log(1 / (1 - x)), 1 / k);
        return y;
    }

    /**
     *   f = k/l * Exp[ - [(x-m)/l]^k ] * [ (x-m)/l ]^(k-1)   
     *   k...p1...parameter of shape
     *   l...p2...parameter of scale
     *   m...p3...parameter of position
     * @author honza
    */
    @Override
    public void setParameters(double p1, double p2, double p3) {
        this.m = p1;
        this.l = p2;
        this.k = 1;
    }

    /**
     * NEFUNGUJE TAK JAK BY MELA!
     * 
     * @param array 
     */
    @Override
    public void setBoundaries(double[] array) {
        this.upP1 = 5;
        this.lowP1 = -5;
        this.upP2 = 5;
        this.lowP2 = 0.0001;
        this.upP3 = 5;
        this.lowP3 = -5;//   0.1;
    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >= lowP1) && (p1 <= upP1) && (p2 > lowP2) && (p2 <= upP2) && (p3 >= lowP3) && (p3 <= upP3));
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
//        d.setParameters(upP3, lowP3, 0);
//        do
//            parameters.p3 = d.getRealization();
//        while (0 >= parameters.p3);
//        return parameters;
//    }
//
//    public Parameters getStandParameters(double[] array, int size) {
//        double m, b0, k, l, t1, t2, W, sumA, sumB, sumC, sumD, z1, z2, dif;
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
//        k = b0;
//        do {
//            sumA = 0;
//            sumB = 0;
//            sumC = 0;
//            sumD = 0;
//            dif = k;
//            for (int i = 0; i < size; i++) {
//                z1 = Math.pow(array[i] - m, k);
//                z2 = Math.log(array[i] - m);
//                sumA += z1;
//                sumB += z1 * z2;
//                sumC += z2;
//                sumD += z1 * z2 * z2;
//            }
//            k = k + (size * k * sumA * sumA - size * k * k * sumA * sumB + k * k * sumA * sumA * sumC) /
//                    (size * sumA * sumA + size * k * k * (sumD * sumA - sumB * sumB));
//            dif = dif - k;
//        }
//        while ((k >= 0) && (dif > 0.001));
//
//        sumA = 0;
//        for (int i = 0; i < size; i++) {
//            sumA += Math.pow(array[i] - m, k);
//        }
//        l = Math.pow(sumA / size, 1 / k);
//
//        parameters.p1 = m;
//        parameters.p2 = l;
//        parameters.p3 = k;
//        return parameters;
//    }

    @Override
    public double getfunctionValue(double x) {
        double y;
        if (x > m) {
            y = (x - m) / l;
            y = (k / l) * Math.pow(y, k - 1) * Math.exp(-Math.pow(y, k));
        } else y = 0.0;
        return y;
    }

    @Override
    public double getFunctionValue(double x) {
        double y;
        if (x > m) {
            y = (x - m) / l;
            y = Math.pow(y, k);
            y = 1 - Math.exp(-y);
        } else y = 0.0;
        return y;
    }
    
    
    @Override
    public String toString() {
        return "Exponential";
    }
}
