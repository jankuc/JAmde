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
     * Returns minimum
     * 
     * @param data
     * @return minimum
     */
    public static double min(double[] data) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < data.length; i++) {
            if (data[i] < min){
              min = data[i];
            }
        }
        return min;
    }
    
    
    /**
     * Returns maximum
     * 
     * @param data
     * @return maximum
     */
    public static double max(double[] data) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > max){
              max = data[i];
            }
        }
        return max;
    }
    
    /**
     * Returns arithmetic mean = MLE of expected value
     * 
     * EV = 1/N SUM (Xi)
     * 
     * @param data
     * @return Arithmetic mean
     */
    public static double getExpVal(double[] data) {
        double EV = 0;
        for (int i = 0; i < data.length; i++) {
            EV += data[i];
        }
        EV = EV / data.length;
        return EV;
    }

    /**
     * Returns standard variation (rozptyl)
     *
     * DV = sqrt( 1/N * SUM ( Xi - mu )^2 )
     * 
     * @param EV
     * @param data
     * @return Standard variation
     */
    public static double getStandVar(double EV, double[] data) {
        double DV = 0;
        for (int i = 0; i < data.length; i++) {
            DV += Math.pow(data[i] - EV, 2);
        }
        DV =  DV / (data.length);
        return DV;
    }
    
    /**
     * Returns standard deviation (směrodatná odchylka)
     * 
     * DV = 1/N * SUM ( Xi - mu )^2
     * 
     * @param EV
     * @param data
     * @return Standard deviation
     */
    public static double getStandDev(double EV, double[] data) {
        double DV = 0;
        for (int i = 0; i < data.length; i++) {
            DV += Math.pow(data[i] - EV, 2);
        }
        DV = Math.sqrt( DV / (data.length));
        return DV;
    }
    
    
    /**
     * Returns Least absolute deviation (absolutní chyba)
     * 
     * DV = sqrt ( 1/N * SUM | Xi - mu | )
     * 
     * @param EV
     * @param data
     * @return Standard deviation
     */
    public static double getLAD(double EV, double[] data) {
        double DV = 0;
        for (int i = 0; i < data.length; i++) {
            DV += Math.abs(data[i] - EV);
        }
        DV = DV / (data.length);
        return DV;
    }

    /**
     *
     * Returns median
     * 
     * @param data
     * @return median
     */
    public static double getMedian(double[] data) {
        Arrays.sort(data);
        if (data.length % 2 == 0) {
            return (data[data.length/2] + data[data.length/2 - 1])/2;
        }
        else {
            return data[(data.length-1)/2];
        }
    }

    /**
     * 
     * Returns MAD - median of the absolute values
     * 
     * @param median
     * @param data
     * @return MAD
     */
    public static double getMAD(double median, double[] data) {
        double newarray[];
        double result;
        newarray = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            newarray[i]= Math.abs(data[i]-median);
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
     * Returns natural logarithm of Gamma function of x
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
    
    /**
     * Returns k-quantile of the data.
     * 
     * @param data
     * @param k in (0,1) is the Quantile we want to use.
     * @return k-quantile of the data
     */
    public static double quantile(double[] data, double k){
        Arrays.sort(data);
        k = k * data.length;
        return data[ (int) Math.ceil(k) - 1];
    }

}
