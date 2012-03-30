/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

/**
 *
 * @author honza
 */
import java.util.Arrays;

/**
 * TODO komentar
 */
public class MathUtil{

    /**
     * 
     * @param value
     * @param precission
     * @return
     */
    public static double round(double value, int precission) {
        double pom = Math.pow(10, precission);
        return Math.round(value * pom) / pom;
    }

    
    /**
     * Returns arithmetic mean = MLE of expected value
     * 
     * EV = 1/N SUM (Xi)
     * 
     * @param array
     * @return Arithmetic mean
     */
    public static double getExpVal(double[] array) {
        double EV = 0;
        for (int i = 0; i < array.length; i++) {
            EV += array[i];
        }
        EV = EV / array.length;
        return EV;
    }

    /**
     * Returns standard variation (rozptyl)
     *
     * DV = 1/N * SUM ( Xi - mu )^2
     * 
     * @param EV
     * @param array
     * @return Standard variation
     */
    public static double getStandVar(double EV, double[] array) {
        double DV = 0;
        for (int i = 0; i < array.length; i++) {
            DV += Math.pow(array[i] - EV, 2);
        }
        DV =  DV / (array.length);
        return DV;
    }
    
    /**
     * Returns standard deviation (směrodatná odchylka)
     * 
     * DV = sqrt ( DV = 1/N * SUM ( Xi - mu )^2 )
     * 
     * @param EV
     * @param array
     * @return Standard deviation
     */
    public static double getStandDev(double EV, double[] array) {
        double DV = 0;
        for (int i = 0; i < array.length; i++) {
            DV += Math.pow(array[i] - EV, 2);
        }
        DV = Math.sqrt( DV / (array.length));
        return DV;
    }

    /**
     *
     * Returns median 
     * 
     * @param array
     * @return median
     */
    public static double getMedian(double[] array) {
        if (array.length % 2 == 0) {
            return (array[array.length/2] + array[array.length/2 - 1])/2;
        }
        else {
            return array[(array.length-1)/2];
        }
    }

    /**
     * 
     * Returns MAD - median of the absolute values
     * 
     * @param median
     * @param array
     * @return MAD
     */
    public static double getMAD(double median, double[] array) {
        double newarray[];
        double result;
        newarray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newarray[i]= Math.abs(array[i]-median);
        }
        Arrays.sort(newarray);
        result =  MathUtil.getMedian(newarray);
        return result;
    }
    
    /**
     * Returns time in string formatted hh:mm:ss
     * 
     * @param runTime
     * @return hh:mm:ss
     */
    public static String Long2time(Long runTime){
        runTime /= 1000;
        Long hours = runTime/3600;
        runTime -= hours*3600;
        Long mins = runTime/60;
        runTime -= mins*60;
        Long secs = runTime;
        return String.format("%02d:%02d:%02d",hours, mins, secs );
    }
    
    /**
     * Returns Gamma function of x.
     * Algorithm for logGamma(x) is from Numerical recipes 6.1
     * 
     * @param x
     * @return Gamma(x)
     */
    public static double gamma(double x){
        return Math.floor(Math.exp(logGamma(x))*10e5 + 0.5)/10e5;
    }
    
    /**
     * Returns natural logarithm of Gamma function
     * Algorithm is from Numerical recipes 6.1
     * 
     * @param x
     * @return log(Gamma(x))
     */
    public static double logGamma(double x) { 
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1)
                + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
                + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
               
        /*
         * TODO zaokrouhlovat
         */
    }

}
