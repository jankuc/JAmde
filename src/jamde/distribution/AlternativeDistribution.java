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
 *    tøída AlternativeDistribution je potomek tøídy Distribution, má metody :
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

public class AlternativeDistribution extends Distribution {
    double pr;


    public AlternativeDistribution(double pr) {
        this.pr = pr;
    }

    @Override
    public double getP1() {
        return pr;
    }

    @Override
    public double getP2() {
        return 0;
    }

    @Override
    public double getP3() {
        return 0;
    }

    @Override
    public double getRealization() {
        double x = Uniform_0_1();
        if (x <= (1 - pr)) return 0.0;
        else return 1.0;
    }

    @Override
    public void setParameters(double p1, double p2, double p3) {
        pr = p1;
    }

    @Override
    public void setBoundaries(double[] array) {
    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >=0.0) && (p1 <= 1.0));
    }
//
//    public Parameters getRandomParameters(Distribution d) {
//        return null;
//    }
//
//    public Parameters getStandParameters(double[] array, int size) {
//        return null;
//    }

    @Override
    public double getfunctionValue(double x) {
        return 0;
    }

    @Override
    public double getFunctionValue(double x) {
        return 0;
    }
    
    @Override
    public String toString() {
        return "Alternative";
    }
}
