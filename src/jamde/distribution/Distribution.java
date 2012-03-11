package jamde.distribution;


import jamde.estimator.Estimator;
import jamde.estimator.EstimatorBuilder;
import java.io.*;

/**
 * TODO komentar
 */

public abstract class Distribution {
    public double UpB1, LowB1, UpB2, LowB2, UpB3, LowB3;
    public double MinEstDist0, MinEstDist, MinEstL1Dist, MinEstHistDist;
    //AmdeInput input;

    /**
     * nastavení poèáteèní hodnoty pro generování náhodných èísel Wichmannovým generátorem, pouije se jen v pøípadì,
     * e na poèátku procesu chceme mít vdy stejnou sadu hodnot a nechceme pouívat vnìjí soubory seed.dta a generconf.dta
     * (tento zpùsob zaruèí jen stejný poèátek pøi kadém sputìní programu, ale ne stejný zaèátek pro kadou tabulku, kdy
     * se poèítají z plánovaèe), nebo naopak chceme mít poèáteèní volbu vdy náhodnou a pak pouijeme random() místo 1000.0
     */

    public static double xX = 1000.0 / 32767;//Math.random();
    public static double yY = 1000.0 / 32767;//Math.random();
    public static double zZ = 1000.0 / 32767;//Math.random();

/*    public static int X = (int) (xX * 30269.0);
    public static int Y = (int) (yY * 30307.0);
    public static int Z = (int) (zZ * 30323.0);
*/

    /**
     * getP1 ... pøedává 1. parametr rozdìlaní
     * getP2 ... pøedává 2. parametr rozdìlaní
     * getP3 ... pøedává 3. parametr rozdìlaní
     * getRealization ... dává jednu realizaci náhodné velièiny pøísluného rozdìlení
     * setParameters ... nastavuje parametry rozdìlaní
     * setBoundaries ... nastavuje hranice parametrického prostoru
     * isParametersOK ... testuje, zda zadané parametry patøí do aktuálnì nastaveného parametrického prostoru
     * getRandomParameters ... vybere náhodnì jeden bod v parametrickém prostoru
     * getStandParameters ... provádí standardní odhad parametrù rozdìlení
     * getFunctionValue ... poèítá funkèní hodnotu distribuèní funkce pøísluného rozdìlení v zadaném bodì metodou
     * inverzní transformace (kromì pøípadu normálního rozdìlení)
     * getfunctionValue ... poèítá funkèní hodnotu hustoty pravdìpodobnosti pøísluného rozdìlení v zadaném bodì
     */
    
    
    public abstract double getP1();
    public abstract double getP2();
    public abstract double getP3();
    public abstract double getRealization();
    public abstract void setParameters(double p1, double p2, double p3);
    public abstract void setBoundaries(double[] array);
    public abstract boolean isParametersOK(double p1, double p2, double p3);
    //public abstract Parameters getRandomParameters(Distribution d);
    //public abstract Parameters getStandParameters(double[] array, int size);
    public abstract double getfunctionValue(double x);
    public abstract double getFunctionValue(double x);
    
    
    public double getDistance(String type, double par, double[] data){
        EstimatorBuilder eB = new EstimatorBuilder(type, par);
        Estimator e = eB.getEstimator();
        return e.countDistance(this, data);
    }
    

//    public Distribution(String type, double par1, double par2) {
//        if (type.equals("Normal")) {
//            
//        }
//    }    
        
    
    
    

    /**
     * Metoda Uniform_0_1 generuje jednu realizaci rovnomìrného rozdìlení
     */

//    public double Uniform_0_1() {
//        double U;
//        double X = Math.random() * Integer.MAX_VALUE;
//        double Y = Math.random() * Integer.MAX_VALUE;
//        double Z = Math.random() * Integer.MAX_VALUE;
//        do {
//            X = (171 * X) % 30269;
//            Y = (172 * Y) % 30307;
//            Z = (170 * Z) % 30323;
//            U = (X * 1.0 / 30269 + Y * 1.0 / 30307 + Z * 1.0 / 30323);
//            U = U - Math.floor(U);
//        } while ((U == 0) || (U == 1));
//
//        return U;
//    }
    
    public double Uniform_0_1(){
        return Math.random();
    }
}
  
//
//    /**
//     * Metoda getKolmDist poèítá Kolmogorovskou vzdálenost distribuèní funkce pøísluného rozdìlení od empirické
//     * distribuèní funkce dané generovanýmy daty.
//     *
//     * @param array
//     * @param size
//     * @return
//     */
//
//    public double getKolmDist(double[] array, int size) {
//        double d = 0.0;
//        double y1, y2, y;
//        for (int i = 0; i < size; i++) {
//            y = getFunctionValue(array[i]);
//            y1 = Math.abs(y - (i * 1.0) / size);
//            y2 = Math.abs(y - ((i + 1) * 1.0) / size);
//            d = Math.max(d, y1);
//            d = Math.max(d, y2);
//        }
//        return d;
//    }
//
//    /**
//     * Metoda getKDist poèítá Kolmogorovskou vzdálenost distribuèní funkce pøísluného rozdìlení od jiné zadané
//     * distribuèní funkce (obvykle to je distribuèní funkce pùvodního zneèiovaného rozdìlení).
//     *
//     * @param d
//     * @return
//     */
//
//    public double getKDist(Distribution d) {
//        double a = 0, b = 0;
//        double x0 = 0, y1, y2, y;
//        double dist = 0;
//
//        do                                   // najde bod, ve kterem jsou obe funkce "dost blizko" nuly a maji
//        {                                    // "dost stejne" funkcni hodnoty
//            a -= 1;
//            y1 = getFunctionValue(a);
//            y2 = d.getFunctionValue(a);
//        }
//        while ((y1 > 0.0001) || (y2 > 0.0001) || (y1 - y2 > 0.0001));
//
//        do                                  // najde bod, ve kterem jsou obe funkce "dost blizko" 1 a maji
//        {                                   // "dost stejne" funkcni hodnoty
//            b += 1;
//            y1 = getFunctionValue(b);
//            y2 = d.getFunctionValue(b);
//        }
//        while ((b <= 1000000) && ((y1 < 0.9999) || (y2 < 0.9999) || (y1 - y2 > 0.0001)));
//
//        double eps = 0.1;
//
//        do {
//            do                              // na intervalu (a,b) najde max. Kolm. vzd. po krocich eps
//            {
//                a += eps;
//                y1 = getFunctionValue(a);
//                y2 = d.getFunctionValue(a);
//                y = Math.abs(y1 - y2);
//                if (y > dist) {
//                    dist = y;
//                    x0 = a;
//                }
//            }
//            while (a < b);
//
//            a = x0 - 5 * eps;               // nastavime novy interval a mensi krok
//            b = x0 + 5 * eps;
//            eps *= 0.5;
//        }
//        while (eps > 0.0001);               // zastavime, kdyz urcime maximum na 4 des.mista
//
//        return dist;
//    }
//
//    /**
//     * Metoda getSubdiverg poèítá hodnotu subdivergence pøísluného rozdìlení od empirické pravdìpodobnostní
//     * míry dané generovanýmy daty.
//     */
//
//    public static double getSubDiverg(double my, double sigma, double m, double s,
//                                      double array[], int size, double par) {
//
//        // sigma a s jsou ve skuteènosti na druhou
//
//        double result;
//        double a;
//        double pom;
//
//        a = par;
//
//        if (a == 0) {
//            result = -Math.log(Math.sqrt(s) / Math.sqrt(sigma));
//        } else {
//            if (a == 1) {
//                result = 0.5 * (Math.pow(my - m, 2) / s - Math.log(sigma / s) + (sigma / s) - 1);
//            } else {
//                result = (a * (a - 1) / 2.0) * (Math.pow(my - m, 2) / (a * s + (1 - a) * sigma) + 1 / (a * (1 - a)) * Math.log((a * s + (1 - a) * sigma) / (Math.pow(s, a) * Math.pow(sigma, 1 - a))));
//                result = (1 / (a - 1)) * (Math.exp(result) - 1);
//            }
//        }
//
//        for (int i = 0; i < size; i++) {
//            if (a == 0) {
//                result = result + (1.0 / (2.0 * size)) * (Math.pow(array[i] - my, 2) / sigma - Math.pow(array[i] - m, 2) / s);
//            } else {
//                pom = (-1.0 * a / 2.0) * (Math.pow(array[i] - my, 2) / sigma - Math.pow(array[i] - m, 2) / s);
//                pom = Math.exp(pom);
//
//                if ((pom == Double.POSITIVE_INFINITY) || (pom == Double.NEGATIVE_INFINITY)) {
//                    pom = pom - 1.0;
//                }
//
//                pom = 1 - Math.pow(s / sigma, a / 2) * pom;
//
//                if ((pom == Double.POSITIVE_INFINITY) || (pom == Double.NEGATIVE_INFINITY)) {
//                    pom = pom - 1.0;
//                }
//
//                pom = (1.0 / (size * a)) * pom;
//
//                if ((pom == Double.POSITIVE_INFINITY) || (pom == Double.NEGATIVE_INFINITY)) {
//                    pom = pom - 1.0;
//                }
//
//                result = result + pom;
//
//                if ((pom == Double.POSITIVE_INFINITY) || (pom == Double.NEGATIVE_INFINITY)) {
//                    result = result - 1.0;
//                }
//            }
//        }
//
//        return result;
//
//    }
//
//    /**
//     * Metoda getSuperdiverg získává hodnotu superdivergence pøísluného rozdìlení od empirické pravdìpodobnostní
//     * míry dané generovanýmy daty.
//     *
//     * @param array
//     * @param size
//     * @param divtype
//     * @param par
//     * @return
//     */
//
//    public double getSuperDiverg(double[] array, int size, char divtype, double par, char LocSca) {
//        double result;
//        double my, sigmapow2;
//
//        my = getP1();
//        sigmapow2 = getP2();
//
//        Distribution d = new NormalDistribution(0, 1);
//        d.setInput(input);
//        d.setBoundaries(array, size);
//        OptMethods m = new OptMethods();
//        result = (-1) * m.Minimalize1D2P_2(d, array, size, '*', par, my, sigmapow2, LocSca);
//        return result;
//    }
//
//
//    /**
//     * Metoda getDivergDist poèítá divergenci hustoty pravdìpodobnosti pøísluného rozdìlení a histogramu daného
//     * generovanýmy daty. Typ divergence je dán pøíznakem divtype. U jedné divergence se pro výpoèet pouívá metoda
//     * výpoètu integrálu integral1 a v pøípadì pouití druhé divergence se pouije metoda výpoètu integerálu integral2.
//     *
//     * @param array
//     * @param size
//     * @param divtype
//     * @return
//     */
//
//    public double getDivergDist(double[] array, int size, char divtype, double par) {
//        double result = 0;
//
//        if (divtype == '1') {
//            double Int = integral1(array, size, par);
//            result = 1 / (par * (par - 1)) * (Int - 1);
//        }
//        if (divtype == '2') {
//            result = 0.5 * integral2(array, size, par);
//        }
//
//        return result;
//    }
//
//
//    /**
//     * Metoda getDDist poèítá divergenci hustoty pravdìpodobnosti pøísluného rozdìlení od jiné zadané hustoty (obvykle
//     * to je hustota pravdìpodobnosti pùvodního zneèiovaného rozdìlení). U jedné divergence se pro výpoèet pouívá metoda
//     * výpoètu integrálu integral3 a v pøípadì pouití druhé divergence se pouije metoda výpoètu integerálu integral4.
//     *
//     * @param d
//     * @param divtype
//     * @return
//     */
//
//    public double getDDist(Distribution d, char divtype) {
//        double par;
//        double result = 0;
//
//        if (divtype == '1') {
//            par = input.getDivergence().getParameterA();
//            double Int = integral3(d, par);
//            if (Int > 1) {
//                result = 0;
//            } else {
//                result = 1 / (par * (par - 1)) * (Int - 1);
//            }
//        }
//        if (divtype == '2') {
//            par = input.getDivergence().getParameterB();
//            result = 0.5 * integral4(d, par);
//        }
//
//        return result;
//    }
//
//    /**
//     * Metoda getDist vrací hodnotu pøísluné vzdálenosti hustoty pravdìpodobnosti od empirické distribuèní funkce nebo
//     * histogramu na základì pøíznaku distype.
//     *
//     * @param array
//     * @param size
//     * @param distype
//     * @return
//     */
//
//    public double getDist(double[] array, int size, char distype, double par, double m, double spow2, char LocSca) {
//        double dist = 0;
//        double p11, p12, p13;
//
//        switch (distype) {
//            case'K':
//                dist = getKolmDist(array, size);
//                break;
//            case'1':
//                dist = getDivergDist(array, size, distype, par);
//                break;
//            case'2':
//                dist = getDivergDist(array, size, distype, par);
//                break;
//            case'P':
//                dist = getSuperDiverg(array, size, distype, par, LocSca);
//                break;
//            case'*':
//                p11 = getP1();
//                p12 = getP2();         // sigma na 2
//                dist = -1 * getSubDiverg(m, spow2, p11, p12, array, size, par);
//                break;
//            case'B':
//                p11 = getP1();
//                p12 = getP2();        // sigma na 2
//                dist = -1 * getSubDiverg(m, spow2, p11, p12, array, size, par);
//                break;
//        }
//
//        return dist;
//    }
//
//    /**
//     * Metoda getL1Dist poèítá L1-vzdálenost pøísluné hustoty pravdìpodobnosti od jiné zadané hustoty (obvykle to je
//     * hustota pùvodního zneèiovaného rozdìlení). Integrál se poèítá obyèejným lichobìníkovým pravidlem. V pøípadì
//     * Weibullova rozdìlení je jistým problémem rozdìlení s parametrem b < 1, které jde u parametru m zprava do nekoneèna.
//     * Pøi integraci v pravém okolí parametru m se toti pouitím lichobìníkového pravidla dopoutíme velké chyby a
//     * do celkového integrálu se pøièítá obrovská hodnota. Zatím je to oetøeno pouze tak, e se tato velká hodnota oøízne.
//     */
//
//    double getL1Dist(Distribution d) {
//        double a1, a2, a3, a4, y1, y2, y3, y4, z;
//
//        double Integral = 0;
//
//        double p1 = d.getP1();
//        double p2 = d.getP2();
//        //double p3 = d.getP3();
//        double p21 = getP1();
//        double p22 = getP2();
//        //double p23 = getP3();
//
//
//        double x0 = p1;
//        double eps = 0.01;
//        int i = 0;
//
//        if ((this instanceof UniformDistribution) && (d instanceof UniformDistribution)) {
//            double p11, p12;
//            p11 = p1;
//            p12 = p2;
//            if ((p12 <= p21) || (p22 <= p11)) {
//                Integral = 2;
//            } else {
//                if ((p11 <= p21) && (p21 <= p12) && (p12 <= p22)) {
//                    Integral = (p21 - p11) / (p12 - p11) + (p12 - p21) * Math.abs(1 / (p12 - p11) - 1 / (p22 - p21)) + (p22 - p12) / (p22 - p21);
//                } else {
//                    if ((p21 <= p11) && (p12 <= p22)) {
//                        Integral = (p11 - p21 + p22 - p12) / (p22 - p21) + (p12 - p11) * Math.abs(1 / (p12 - p11) - 1 / (p22 - p21));
//                    } else {
//                        if ((p11 <= p21) && (p22 <= p12)) {
//                            Integral = (p21 - p11 + p12 - p22) / (p12 - p11) + (p22 - p21) * Math.abs(1 / (p12 - p11) - 1 / (p22 - p21));
//                        } else {
//                            if ((p21 <= p11) && (p11 <= p22) && (p22 <= p12)) {
//                                Integral = (p11 - p21) / (p22 - p21) + (p22 - p11) * Math.abs(1 / (p12 - p11) - 1 / (p22 - p21)) + (p12 - p22) / (p22 - p21);
//                            } else {
//
//                                /*
//                                TODO dodelat alert
//                               MessageBox(NULL,"Chybka ve výpoètu L1 vzdálenosti dvou rovnomìrných rozdìlení.","Chyba výpoètu",
//                                MB_ICONWARNING|MB_OK|MB_TASKMODAL);
//                                */
//                                Integral = 0;
//                            }
//                        }
//                    }
//                }
//            }
//        } else {
//
//            if (this instanceof WeibullDistribution) {
//                do {
//                    y1 = getfunctionValue(x0 + i * eps) - d.getfunctionValue(x0 + i * eps);
//                    y1 = Math.abs(y1);
//                    if (y1 > 1) {
//                        y1 = 1;
//                    }
//                    a1 = getfunctionValue(x0 + (i + 1) * eps);
//                    a2 = d.getfunctionValue(x0 + (i + 1) * eps);
//                    y2 = a1 - a2;
//                    y2 = Math.abs(y2);
//                    if (y2 > 1) {
//                        y2 = 1;
//                    }
//                    y3 = getfunctionValue(x0 - i * eps) - d.getfunctionValue(x0 - i * eps);
//                    y3 = Math.abs(y3);
//                    if (y3 > 1) {
//                        y3 = 1;
//                    }
//                    a3 = getfunctionValue(x0 - (i + 1) * eps);
//                    a4 = d.getfunctionValue(x0 - (i + 1) * eps);
//                    y4 = a3 - a4;
//                    y4 = Math.abs(y4);
//                    if (y4 > 1) {
//                        y4 = 1;
//                    }
//
//                    z = 0.5 * eps * (y1 + y2 + y3 + y4);
//                    Integral += z;
//                    i++;
//                }
//                while ((a1 > 0.0001) || (a2 > 0.0001) || (a3 > 0.0001) || (a4 > 0.0001) || (i < 10));
//            } else {
//                do {
//                    y1 = getfunctionValue(x0 + i * eps) - d.getfunctionValue(x0 + i * eps);
//                    y1 = Math.abs(y1);
//                    a1 = getfunctionValue(x0 + (i + 1) * eps);
//                    a2 = d.getfunctionValue(x0 + (i + 1) * eps);
//                    y2 = a1 - a2;
//                    y2 = Math.abs(y2);
//                    y3 = getfunctionValue(x0 - i * eps) - d.getfunctionValue(x0 - i * eps);
//                    y3 = Math.abs(y3);
//                    a3 = getfunctionValue(x0 - (i + 1) * eps);
//                    a4 = d.getfunctionValue(x0 - (i + 1) * eps);
//                    y4 = a3 - a4;
//                    y4 = Math.abs(y4);
//
//                    z = 0.5 * eps * (y1 + y2 + y3 + y4);
//                    Integral += z;
//                    i++;
//                }
//                while ((a1 > 0.0001) || (a2 > 0.0001) || (a3 > 0.0001) || (a4 > 0.0001) || (i < 10));
//            }
//        }
//
//        return Integral;
//    }
//
//    /**
//     * Metoda getHistDist poèítá L1-vzdálenost hustoty pravdìpodobnosti pøísluného rozdìlení od histogramu daného
//     * generovanýmy daty. Integrál se poèítá obyèejným lichobìníkovým pravidlem. Pro výpoèet u Weibullova rozdìlení
//     * zde platí toté jako u metody getL1Dist.
//     */
//
//    double getHistDist(double[] array, int size) {
//        double y1, y2, y3, y4;
//        double x0 = array[0];
//        double eps = 0.01;
//        double h;
//        double Integral = 0;
//
//        // rùzné monosti, jak nastavit délku buòky v histogramu
//
///*        int index = (int) MathUtil.Round(size * 0.25, 0);
//        double x_25 = array[index];
//        index = (int) MathUtil.Round(size * 0.75, 0);
//        double x_75 = array[index];
//        double exp = -1.0 / 3.0;
//        double d = (x_75 - x_25) * Math.pow(size, exp);       // 2*
//        d = MathUtil.Round(d, 2);
//*/
///*        double EV, DV;
//        EV = MathUtil.getExpVal(array, size);
//        DV = MathUtil.getStandDev(EV, array, size, input);
//        double exp = -1.0 / 3.0;
//        double d = 3.49 * DV * Math.pow(size, exp);
//        d = MathUtil.Round(d, 2);
//*/
///*        int index = (int) MathUtil.Round(size * 0.25, 0);
//        double x_25 = array[index];
//        index = (int) MathUtil.Round(size * 0.75, 0);
//        double x_75 = array[index];
//        double exp = -1.0 / 3.0;
//        double d = 3.49 * ((x_75 - x_25) / 2) * Math.pow(size, exp);
//        d = MathUtil.Round(d, 2);
//*/
//
//        double d;
//        if (this instanceof CauchyDistribution) {
//            int index = (int) Math.round(size * 0.25);
//            double x_25 = array[index];
//            index = (int) Math.round(size * 0.75);
//            double x_75 = array[index];
//            switch (size) {
//                case 10:
//                    d = 2.8 * ((x_75 - x_25) / 2);        //4.0
//                    break;
//                case 20:
//                    d = 2.7 * ((x_75 - x_25) / 2);        //3.5
//                    break;
//                case 50:
//                    d = 2.4 * ((x_75 - x_25) / 2);        //2.5
//                    break;
//                case 120:
//                    d = 2.1 * ((x_75 - x_25) / 2);       //1.8
//                    break;
//                case 250:
//                    d = 1.6 * ((x_75 - x_25) / 2);       //1.5
//                    break;
//                default:
//                    d = (-0.00478 * size + 2.7957) * ((x_75 - x_25) / 2);
//                    break;
//            }
//        } else {
//            if (this instanceof UniformDistribution) {
///*                int index =(int) MathUtil.Round (size * 0.25 , 0);
//                double x_25 = array[index];
//                index = (int) MathUtil.Round (size * 0.75 , 0);
//                double x_75 = array[index];
//                double exp = -1.0/3.0;
//                 d = 0.8 * 2.0*(x_75-x_25)* pow(size,exp);
//*/
//                d = 0.2;
//            } else {
//                double EV, DV;
//                EV = MathUtil.getExpVal(array, size);
//                DV = MathUtil.getStandDev(EV, array, size, input);
//                double exp = -1.0 / 3.0;
//                d = 3.49 * DV * Math.pow(size, exp);
//            }
//        }
//        d = MathUtil.Round(d, 1);
//        // d=0.2;
//
//        do                                   // najde bod nalevo od prvni generovane hodnoty, ve
//        {                                    // kterem je dist. funkce "dost blizko" nuly
//            y1 = getfunctionValue(x0);
//            x0 -= 1;
//        }
//        while (y1 > 0.0001);
//
//        x0 = Math.floor(x0 + 1);
//
//        int i = 0;
//
//        if (this instanceof WeibullDistribution) {
//            do {
//                int count = 0;                                      // pocita kolik hodnot je v intervalu < x0,x0+d >
//                if ((i <= size - 1) && (array[i] < x0 + d)) {
//                    while ((i <= size - 1) && (array[i] >= x0) && (array[i] < x0 + d)) {
//                        count++;
//                        i++;
//                    }
//                }
//
//                h = count / (size * d);                              // hodnota histogramu v < x0,x0+d >
//
//                double end = MathUtil.Round(x0 + d, 3);              // integral |f(x)-h| na < x0,x0+d >
//                do {
//                    y1 = getfunctionValue(x0);
//                    y2 = Math.abs(y1 - h);
//                    if (y2 > 1) {
//                        y2 = 1;
//                    }
//                    y3 = getfunctionValue(x0 + eps);
//                    y4 = Math.abs(y3 - h);
//                    if (y4 > 1) {
//                        y4 = 1;
//                    }
//
//                    Integral += 0.5 * eps * (y2 + y4);
//                    x0 = MathUtil.Round(x0 + eps, 3);
//                }
//                while (x0 < end);
//            }
//            while ((y3 > 0.0001) || (x0 <= array[size - 1]));  // pocitani ukoncime v bode napravo od posledni generovane
//            // hodnoty, ve kterem je dist. funkce "dost blizko" 0
//        } else {
//            do {
//                int count = 0;                                 // pocita kolik hodnot je v intervalu < x0,x0+d >
//                if ((i <= size - 1) && (array[i] < x0 + d)) {
//                    while ((i <= size - 1) && (array[i] >= x0) && (array[i] < x0 + d)) {
//                        count++;
//                        i++;
//                    }
//                }
//
//                h = count / (size * d);                        // hodnota histogramu v < x0,x0+d >
//
//                double end = MathUtil.Round(x0 + d, 3);        // integral |f(x)-h| na < x0,x0+d >
//                do {
//                    y1 = getfunctionValue(x0);
//                    y2 = Math.abs(y1 - h);
//                    y3 = getfunctionValue(x0 + eps);
//                    y4 = Math.abs(y3 - h);
//
//                    Integral += 0.5 * eps * (y2 + y4);
//                    x0 = MathUtil.Round(x0 + eps, 3);
//                }
//                while (x0 < end);
//            }
//            while ((y3 > 0.0001) || (x0 <= array[size - 1]));    // pocitani ukoncime v bode napravo od posledni generovane
//            //  hodnoty, ve kterem je dist. funkce "dost blizko" 0
//        }
//
//        return Integral;
//    }
//
//    /**
//     * Metody countL1Dist, countKDist, countDDist, countHistDist pouze pøiøazují spoèítané hodnoty vzdáleností
//     * pøísluným atributùm tøídy Distribution.
//     */
//
//    public void countL1Dist(Distribution d) {
//        MinEstL1Dist = getL1Dist(d);
//    }
//
//    public void countKDist(Distribution d) {
//        MinEstDist = getKDist(d);
//    }
//
//    public void countDDist(Distribution d, char divtype) {
//        MinEstDist = getDDist(d, divtype);
//    }
//
//    public void countHistDist(double[] array, int size) {
//        MinEstHistDist = getHistDist(array, size);
//    }
//
//    /*
//    *  Metody integral1, integral2  poèítají integrály pøísluející k jednotivým typùm divergencí. Jsou volané pouze
//    *  metodou getDivergDist a proto jsou privátní. Lze také rùznì nastavit krok v histogramu a krok v integraci.
//    */
//
//    private double integral1(double[] array, int size, double a) {
//        double y1, y2;
//        double x0 = array[0];
//        double eps = 0.1;
//        double h;
//        double Integral = 0;
//
//        // rùzné monosti, jak nastavit délku buòky v histogramu
//
//        //  double d = 2.0;
//
///*        int index = (int) MathUtil.Round (size * 0.25 , 0);
//        double x_25 = array[index];
//        index = (int) MathUtil.Round (size * 0.75 , 0);
//        double x_75 = array[index];
//        double exp = -1.0/3.0;
//        double d = 2*(x_75-x_25)* pow(size,exp);   //
//        d = MathUtil.Round(d,2);
//*/
//
//        double d;
//        if (this instanceof CauchyDistribution) {
//            int index = (int) Math.round(size * 0.25);
//            double x_25 = array[index];
//            index = (int) Math.round(size * 0.75);
//            double x_75 = array[index];
//            switch (size) {
//                case 10:
//                    d = 2.8 * ((x_75 - x_25) / 2);
//                    break;
//                case 20:
//                    d = 2.7 * ((x_75 - x_25) / 2);
//                    break;
//                case 50:
//                    d = 2.4 * ((x_75 - x_25) / 2);
//                    break;
//                case 120:
//                    d = 2.1 * ((x_75 - x_25) / 2);
//                    break;
//                case 250:
//                    d = 1.6 * ((x_75 - x_25) / 2);
//                    break;
//                default:
//                    d = (-0.00478 * size + 2.7957) * ((x_75 - x_25) / 2);
//                    break;
//            }
//        } else {
//            if (this instanceof UniformDistribution) {
///*                int index =(int) MathUtil.Round (size * 0.25 , 0);
//               double x_25 = array[index];
//               index = (int) MathUtil.Round (size * 0.75 , 0);
//               double x_75 = array[index];
//               double exp = -1.0/3.0;
//               d = 0.8 * 2.0*(x_75-x_25)* pow(size,exp);
//*/
//                d = 0.2; //
//            } else {
//                double EV, DV;
//                EV = MathUtil.getExpVal(array, size);
//                DV = MathUtil.getStandDev(EV, array, size, input);
//                double exp = -1.0 / 3.0;
//                d = 3.49 * DV * Math.pow(size, exp);
//            }
//        }
//        d = MathUtil.Round(d, 1);
//
//        x0 = Math.floor(x0);
//
//        int i = 0;
//
//        if (this instanceof UniformDistribution)         // pro Uniform rozdìlení lze vyuít toho, e je to po èástech konst. funkce
//        {
//            double f;
//            double p1 = getP1();
//            double p2 = getP2();
//            //double p3 = getP3();
//            f = getfunctionValue((p2 + p1) / 2.0);
//            f = Math.pow(f, 1 - a);
//
//            double x1, x2;
//            do {
//                int count = 0;                                 // pocita kolik hodnot je v intervalu < x0,x0+d >
//                if ((i <= size - 1) && (array[i] < x0 + d)) {
//                    while ((i <= size - 1) && (array[i] >= x0) && (array[i] < x0 + d)) {
//                        count++;
//                        i++;
//                    }
//                }
//
//                h = count / (size * d);                     // hodnota histogramu v < x0,x0+d >
//                h = Math.pow(h, a);
//
//                x1 = Math.max(p1, x0);
//                x2 = Math.min(x0 + d, p2);
//                if (x2 > x1) Integral += (x2 - x1) * f * h;
//
//                x0 = x0 + d;
//            }
//            while (x0 < array[size - 1]);                   // pocitani ukoncime tam, odkud je histogram 0
//        } else {
//            if (this instanceof WeibullDistribution)        // pro Weibulla je tøeba omezit hodnoty
//            {
//                do {
//                    int count = 0;                          // pocita kolik hodnot je v intervalu < x0,x0+d >
//                    if ((i <= size - 1) && (array[i] < x0 + d)) {
//                        while ((i <= size - 1) && (array[i] >= x0) && (array[i] < x0 + d)) {
//                            count++;
//                            i++;
//                        }
//                    }
//
//                    h = count / (size * d);                 // hodnota histogramu v < x0,x0+d >
//                    double end = MathUtil.Round(x0 + d, 3);
//
//                    if (h != 0)                             // integral na < x0,x0+d >
//                    {
//                        h = Math.pow(h, a);
//                        y1 = getfunctionValue(x0);
//                        y1 = Math.pow(y1, 1 - a);
//                        if (y1 > 1) {
//                            y1 = 1;
//                        }
//                        do {
//                            y2 = getfunctionValue(x0 + eps);
//                            y2 = Math.pow(y2, 1 - a);
//                            if (y2 > 1) {
//                                y2 = 1;
//                            }
//                            Integral += 0.5 * eps * (y1 + y2) * h;
//                            x0 = MathUtil.Round(x0 + eps, 3);
//                            y1 = y2;
//                        }
//                        while (x0 < end);
//                    } else {
//                        x0 = end;
//                    }
//                }
//                while (x0 < array[size - 1]);               // pocitani ukoncime tam, odkud je histogram 0
//            } else {
//                do {
//                    int count = 0;                          // pocita kolik hodnot je v intervalu < x0,x0+d >
//                    if ((i <= size - 1) && (array[i] < x0 + d)) {
//                        while ((i <= size - 1) && (array[i] >= x0) && (array[i] < x0 + d)) {
//                            count++;
//                            i++;
//                        }
//                    }
//
//                    h = count / (size * d);                // hodnota histogramu v < x0,x0+d >
//                    double end = MathUtil.Round(x0 + d, 3);
//
//                    if (h != 0) {
//                        h = Math.pow(h, a);
//                        y1 = getfunctionValue(x0);
//                        y1 = Math.pow(y1, 1 - a);
//                        do                                        // integral na < x0,x0+d >
//                        {
//                            y2 = getfunctionValue(x0 + eps);
//                            y2 = Math.pow(y2, 1 - a);
//                            Integral += 0.5 * eps * (y1 + y2) * h;
//                            x0 = MathUtil.Round(x0 + eps, 3);
//                            y1 = y2;
//                        }
//                        while (x0 < end);
//                    } else {
//                        x0 = end;
//                    }
//                }
//                while (x0 < array[size - 1]);            // pocitani ukoncime tam, odkud je histogram 0
//            }
//        }
//
//        if (Integral > 1) {
//            Integral = 1;
//        }
//
//        return Integral;
//    }
//
//
//    private double integral2(double[] array, int size, double b) {
//        double y1, y2;
//
//        double eps = 0.1;
//        double h;
//        double Integral = 0;
//
//        //  rùzné monosti, jak nastavit délku buòky v histogramu
//
///*        int index = (int) MathUtil.Round(size * 0.25, 0);
//        double x_25 = array[index];
//        index = (int) MathUtil.Round(size * 0.75, 0);
//        double x_75 = array[index];
//        double exp = -1.0 / 3.0;
//        double d = (x_75 - x_25) * Math.pow(size, exp);       // 2*
//        d = MathUtil.Round(d, 2);
//*/
///*        double EV, DV;
//        EV = MathUtil.getExpVal(array, size);
//        DV = MathUtil.getStandDev(EV, array, size, input);
//        double exp = -1.0 / 3.0;
//        double d = 3.49 * DV * Math.pow(size, exp);
//        d = MathUtil.Round(d, 2);
//*/
///*        int index = (int) MathUtil.Round(size * 0.25, 0);
//        double x_25 = array[index];
//        index = (int) MathUtil.Round(size * 0.75, 0);
//        double x_75 = array[index];
//        double exp = -1.0 / 3.0;
//        double d = 3.49 * ((x_75 - x_25) / 2) * Math.pow(size, exp);
//        d = MathUtil.Round(d, 2);
//*/
//
//        double d;
//        if (this instanceof CauchyDistribution)               // pro Cauchyho ja tøeba zvlátní volba
//        {
//            int index = (int) Math.round(size * 0.25);
//            double x_25 = array[index];
//            index = (int) Math.round(size * 0.75);
//            double x_75 = array[index];
//            switch (size) {
//                case 10:
//                    d = 2.8 * ((x_75 - x_25) / 2);
//                    break;
//                case 20:
//                    d = 2.7 * ((x_75 - x_25) / 2);
//                    break;
//                case 50:
//                    d = 2.4 * ((x_75 - x_25) / 2);
//                    break;
//                case 120:
//                    d = 2.1 * ((x_75 - x_25) / 2);
//                    break;
//                case 250:
//                    d = 1.6 * ((x_75 - x_25) / 2);
//                    break;
//                default:
//                    d = (-0.00478 * size + 2.7957) * ((x_75 - x_25) / 2);
//            }
//        } else {
//            if (this instanceof UniformDistribution) {
///*                int index = (int) MathUtil.Round(size * 0.25, 0);
//                double x_25 = array[index];
//                index = (int) MathUtil.Round(size * 0.75, 0);
//                double x_75 = array[index];
//                double exp = -1.0 / 3.0;
//                d = 0.8 * 2.0 * (x_75 - x_25) * Math.pow(size, exp);
//*/
//                d = 0.2; //
//            } else {
//                double EV, DV;
//                EV = MathUtil.getExpVal(array, size);                     //for uncontaminated data
//                DV = MathUtil.getStandDev(EV, array, size, input);        //this choice works better
//                //EV = MathUtil.getMedian(array, size);
//                //DV = MathUtil.getMAD(EV, array, size, input);
//                double exp = -1.0 / 3.0;
//                d = 3.49 * DV * Math.pow(size, exp);
//            }
//        }
//        d = MathUtil.Round(d, 1);
//        //  d = 0.2;
//
//        // samotný výpoèet
//        double x0 = array[0];
//        x0 = Math.floor(x0);                                    // nastavení na zaèátek histogramu
//
//        double p1 = getP1();
//        //double p2 = getP2();
//        //double p3 = getP3();
//
//        y1 = getfunctionValue(x0);                                 // Poèítá hodnotu integrálu nalevo od první generované hodnoty,
//        if ((this instanceof WeibullDistribution) && (y1 > 1))     // kde je histogram roven 0. Je-li hustota pøíli vlevo od
//        {                                                          // histogramu, zapoèítáme rovnou celý integrál z f, co je 1.
//            y1 = 1;
//        }
//        if ((y1 < 0.0001) && (x0 > p1))
//        {
//            Integral = 1;
//        } else {
//            do {
//                y2 = getfunctionValue(x0 - eps);
//                if ((this instanceof WeibullDistribution) && (y2 > 1)) {
//                    y2 = 1;
//                }
//                Integral += 0.5 * eps * (y1 + y2);
//                y1 = y2;
//                x0 = MathUtil.Round(x0 - eps, 3);
//            }
//            while (y2 > 0.0001);
//        }
//
//        Integral = Integral * (1 / (1 - b));           // to je ze vzorce pro integrál2 je-li f=0
//
//        x0 = array[0];                                 // znovu nastavíme na zaèátek histogramu
//        x0 = Math.floor(x0);
//
//        int i = 0;
//        double inc = d;
//
//        do {
//
//            int count = 0;
//            d = inc;                                         // pocita kolik gener. hodnot je v intervalu < x0,x0+d >
//            do {
//                if ((i <= size - 1) && (array[i] < x0 + d))
//                {
//                    while ((i <= size - 1) && (array[i] >= x0) && (array[i] < x0 + d)) {
//                        count++;
//                        i++;
//                    }
//                }
//                if ((count == 0) && (i <= size - 1)) d = d + inc;
//            }
//            while ((count == 0) && (i <= size - 1));
//
//            h = count / (size * d);                        // hodnota histogramu v < x0,x0+d >
//
//            double end = MathUtil.Round(x0 + d, 3);
//
//            y1 = getfunctionValue(x0);                    // integral na < x0,x0+d >
//            if ((y1 == 0) && (h == 0)) {
//                y1 = 0;
//            } else {
//                y1 = Math.pow(h - y1, 2) / (b * h + (1 - b) * y1);
//            }
//            if ((this instanceof WeibullDistribution) && (y1 > 1)) {
//                y1 = 1;
//            }
//
//            do {
//                y2 = getfunctionValue(x0 + eps);
//                if ((y2 == 0) && (h == 0)) {
//                    y2 = 0;
//                } else {
//                    y2 = Math.pow(h - y2, 2) / (b * h + (1 - b) * y2);
//                }
//                if ((this instanceof WeibullDistribution) && (y2 > 1)) {
//                    y2 = 1;
//                }
//
//                Integral += 0.5 * eps * (y1 + y2);
//                x0 = MathUtil.Round(x0 + eps, 3);
//                y1 = y2;
//            }
//            while (x0 < end);
//        }
//        while (x0 < array[size - 1]);                 // pocitani ukoncime tam, odkud je histogram 0
//
//
//        y1 = getfunctionValue(x0);                                 // Poèítá hodnotu integrálu napravo od poslední generované hodnoty,
//        if ((this instanceof WeibullDistribution) && (y1 > 1))     // kde je histogram roven 0. Je-li hustota pøíli vpravo od
//        {                                                          // histogramu, zapoèítáme rovnou integrál z f, co je 1, násobený 1/b.
//            y1 = 1;
//        }
//        if ((y1 < 0.0001) && (x0 < p1))
//        {
//            Integral += 1 / (1 - b);
//        } else {
//            do {
//                y2 = getfunctionValue(x0 + eps);
//                if ((this instanceof WeibullDistribution) && (y2 > 1)) {
//                    y2 = 1;
//                }
//                Integral += 0.5 * eps * (y1 + y2) * (1 / (1 - b));
//                y1 = y2;
//                x0 = MathUtil.Round(x0 + eps, 3);
//            }
//            while (y2 > 0.0001);
//        }
//
//        double limit = 1 / (1 - b) + 1 / b;
//
//        if (Integral > limit) {
//            Integral = limit;
//        }
//
//        return Integral;
//    }
//
//    /**
//     * Metody integral3 a integral4 poèítají integrály pøísluející k jednotivým typùm divergencí. Jsou volané pouze
//     * metodou getDDist a proto jsou privátní.
//     *
//     * @param d
//     * @param a
//     * @return
//     */
//
//    double integral3(Distribution d, double a) {
//        double y1, y2, y3, y4, z1, z2, x0;
//        double eps = 0.01;
//        double Integral = 0;
//
//        double p1 = d.getP1();
//        //double p2 = d.getP2();
//        //double p3 = d.getP3();
//
//        x0 = p1 + eps;
//        do                      // najde bod, ve kterém jsou obì funkce "dost blízko" nuly a mají "dost stejné" funkèní hodnoty
//        {
//            x0 -= eps;
//            y1 = getfunctionValue(x0);
//            y2 = d.getfunctionValue(x0);
//        }
//        while ((y1 > 0.0001) || (y2 > 0.0001));
//
//        x0 = MathUtil.Round(x0, 1);
//
//        y1 = getfunctionValue(x0);
//        y1 = Math.pow(y1, a);
//        y2 = d.getfunctionValue(x0);
//        y2 = Math.pow(y2, 1 - a);
//        do {
//            z1 = getfunctionValue(x0 + eps);
//            y3 = Math.pow(z1, a);
//            z2 = d.getfunctionValue(x0 + eps);
//            y4 = Math.pow(z2, 1 - a);
//            Integral += 0.5 * eps * (y1 * y2 + y3 * y4);
//            x0 = MathUtil.Round(x0 + eps, 3);
//            y1 = y3;
//            y2 = y4;
//        }
//        while ((z1 > 0.0001) || (z2 > 0.0001));
//
//        return Integral;
//    }
//
//
//    double integral4(Distribution d, double b) {
//        double y1, y2, y3, y4, z1, z2, x0;
//        double eps = 0.01;
//        double Integral = 0;
//
//        double p1 = d.getP1();
//        //double p2 = d.getP2();
//        //double p3 = d.getP3();
//
//        x0 = p1 + eps;
//        do                         // najde bod, ve kterém jsou obì funkce "dost blízko" nuly a mají "dost stejné" funkèní hodnoty
//        {
//            x0 -= eps;
//            y1 = getfunctionValue(x0);
//            y2 = d.getfunctionValue(x0);
//        }
//        while ((y1 > 0.0001) || (y2 > 0.0001));
//
//        x0 = MathUtil.Round(x0, 1);
//
//        y1 = getfunctionValue(x0);
//        y2 = d.getfunctionValue(x0);
//        if ((y1 == 0) && (y2 == 0)) {
//            z1 = 0;
//        } else {
//            z1 = Math.pow(y1 - y2, 2) / (b * y1 + (1 - b) * y2);
//        }
//        do {
//            y3 = getfunctionValue(x0 + eps);
//            y4 = d.getfunctionValue(x0 + eps);
//            if ((y3 == 0) && (y4 == 0)) {
//                z2 = 0;
//            } else {
//                z2 = Math.pow(y3 - y4, 2) / (b * y3 + (1 - b) * y4);
//            }
//            Integral += 0.5 * eps * (z1 + z2);
//            x0 = MathUtil.Round(x0 + eps, 3);
//            z1 = z2;
//        }
//        while ((y3 > 0.0001) || (y4 > 0.0001));
//
//        return Integral;
//    }
//
//    public void setInput(AmdeInput input) {
//        this.input = input;
//    }
//}
