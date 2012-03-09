/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.distribution;

/**
 *    tøída NormalDistribution je potomek tøídy Distribution, má metody :
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


public class NormalDistribution extends Distribution {
    public static String IDENTIFICATION="Normal";
    double mu, sigma;
    double PI;
    double a[] = new double[6];

    private double N_0_1() {
        double x1 = Uniform_0_1();
        double x2 = Uniform_0_1();
        double y1 = Math.sin(2 * PI * x2) * Math.sqrt((-2.0) * Math.log(x1));
        //  double y2 = cos(2*PI*x2) * sqrt((-2)*log(x1));
        return y1;
    }

    public NormalDistribution(double mu, double sigma) {
        this.mu = mu;
        this.sigma = Math.sqrt(sigma);
        this.UpB1 = 5;
        this.LowB1 = -5;
        this.UpB2 = 100;
        this.LowB2 = 0;
        this.PI = 3.141593;
        this.a[0] = 0.2316419;
        this.a[1] = 0.3193815;
        this.a[2] = -0.3565638;
        this.a[3] = 1.781478;
        this.a[4] = -1.821256;
        this.a[5] = 1.330274;
    }

    @Override
    public double getP1() {
        return mu;
    }

    @Override
    public double getP2() {
        return Math.pow(sigma, 2);
    }

    @Override
    public double getP3() {
        return 0;
    }

    @Override
    public double getRealization() {
        double x = N_0_1();
        return sigma * x + mu;
    }

    @Override
    public void setParameters(double p1, double p2, double p3){
        mu = p1;
        sigma = Math.sqrt(p2);
    }

    @Override
    public void setBoundaries(double[] array) {
        double EV;
        double DV;
        this.LowB1 = -5;
        this.UpB1 = 5;
        this.LowB2 = 0.0001;
        this.UpB2 = 10;
                

        //char LocSca = input.getEstimationModel().charAt(0);
//        switch (LocSca) {
//            case'L':
//                this.LowB1 = -5;
//                this.UpB1 = 5;
//                this.LowB2 = input.getContaminatedDistribution().getParameter2();
//                this.UpB2 = input.getContaminatedDistribution().getParameter2();
//                break;
//            case'S':
//                this.UpB1 = input.getContaminatedDistribution().getParameter1();
//                this.LowB1 = input.getContaminatedDistribution().getParameter1();
//                this.LowB2 = 0;
//                this.UpB2 = 5;                          // pro nekontaminovaná data, ale jen rozdìlení s malým rozptylem, tj. ne Cauchy
//
//                EV = MathUtil.getExpVal(array);
//                DV = MathUtil.getStandDev(EV, array);
//                this.UpB2 = Math.ceil(Math.pow(DV, 2) + 5);
///*                if (Math.pow(DV, 2) - 5 > 0) {
//                    this.LowB2 = Math.floor(Math.pow(DV, 2) - 5);
//                } else {
//                    this.LowB2 = 0;
//                }
//*/
//                break;
//            case'B':
///*              this.LowB1 = -5;            // pro nekontaminovaná data, ale jen rozdìlení s malým rozptylem, tj. ne Cauchy
//                this.UpB1 = 5;
//                this.UpB2 = 5;
//*/
//                this.LowB2 = 0;
//
//                //EV = MathUtil.getExpVal(array, size);
//                EV = MathUtil.getMedian(array);
//                //DV = MathUtil.getStandDev(EV, array, size, input);
//                DV = MathUtil.getMAD(EV, array);
//                this.LowB1 = Math.floor(EV-5);
//                this.UpB1 = Math.ceil(EV+5);
//                this.UpB2 = Math.ceil(Math.pow(DV, 2) + 5);
// /*               if (Math.pow(DV, 2) - 5 > 0) {
//                    this.LowB2 = Math.floor(Math.pow(DV, 2) - 5);
//                } else {
//                    this.LowB2 = 0;
//                }
//*/
//                break;
//        }

    }

    @Override
    public boolean isParametersOK(double p1, double p2, double p3) {
        return ((p1 >= LowB1) && (p1 <= UpB1) && (p2 > LowB2) && (p2 <= UpB2));
    }

//    public Parameters getRandomParameters(Distribution d) {
//        Parameters parameters = new Parameters();
//        d.setParameters(UpB1, LowB1, 0);
//        parameters.p1 = d.getRealization();
//        d.setParameters(UpB2, LowB2, 0);
//        do
//            parameters.p2 = d.getRealization();
//        while (0 >= parameters.p2);
//        return parameters;
//    }
//
//    public Parameters getStandParameters(double[] array, int size) {
//        Parameters parameters = new Parameters();
//        double EV, DV;
//        EV = MathUtil.getExpVal(array, size);
//        DV = MathUtil.getStandDev(EV, array, size, input);
//        parameters.p1 = EV;
//        parameters.p2 = Math.pow(DV, 2);
//        return parameters;
//    }

    @Override
    public double getfunctionValue(double x) {
        double odm = 1 / Math.sqrt(2 * PI * Math.pow(sigma, 2));
        double y = (Math.pow(x - mu, 2)) / (2 * Math.pow(sigma, 2));
        y = Math.exp(-y);
        y = odm * y;
        return y;
    }

    @Override
    public double getFunctionValue(double x) {
        return Phi((x - mu) / sigma);
    }

    double Phi(double x) {
        double y;
        double w;
        double phi;

        y = Math.exp(-Math.pow(x, 2) / 2);
        double odm = 1 / Math.sqrt(2 * PI);
        phi = odm * y;
        y = 0.0;
        w = 1 / (1 + a[0] * Math.abs(x));
        for (int i = 1; i < 6; i++) {
            y = y + a[i] * Math.pow(w, i);
        }
        if (x >= 0) {
            y = 1 - phi * y;
        } else {
            y = phi * y;
        }

        return y;
    }
    
    @Override
    public String toString() {
        return "Normal";
    }
    
}
