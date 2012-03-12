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

    public static double Abs (double x){
        if (x>=0.0) return x;
       else return -x;
    }

    public static double Round(double value, int precission) {
        double pom = Math.pow(10, precission);
        return Math.round(value * pom) / pom;
    }

    public static double Min2(double a, double b) {
        if (a < b) return a;
        else return b;
    }

    public static double Max2(double a, double b) {
        if (a > b) return a;
        else return b;
    }

    public static double Max3(double a, double b, double c) {
        double m = Max2(a, b);
        m = Max2(m, c);
        return m;
    }

    public static double Max4(double a, double b, double c, double d) {
        double m = Max3(a, b, c);
        m = Max2(m, d);
        return m;
    }

    public static double Max6(double a, double b, double c, double d, double e, double f) {
        double m = Max3(a, b, c);
        m = Max4(m, d, e, f);
        return m;
    }

    public static double getExpVal(double[] array) {
        double EV = 0;
        for (int i = 0; i < array.length; i++) {
            EV += array[i];
        }
        EV = EV / array.length;
        return EV;
    }

    public static double getStandVar(double EV, double[] array) {
        double DV = 0;
        for (int i = 0; i < array.length; i++) {
            DV += Math.pow(array[i] - EV, 2);
        }
        DV =  DV / (array.length);
        return DV;
    }
    
    public static double getStandDev(double EV, double[] array) {
        double DV = 0;
        for (int i = 0; i < array.length; i++) {
            DV += Math.pow(array[i] - EV, 2);
        }
        DV = Math.sqrt( DV / (array.length));
        return DV;
    }

    public static double getMedian(double[] array) {
        if (array.length % 2 == 0) {
            return (array[array.length/2] + array[array.length/2 - 1])/2;
        }
        else {
            return array[(array.length-1)/2];
        }
    }

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
    
    public static String Long2time(Long runTime){
        runTime /= 1000;
        Long hours = runTime/3600;
        runTime -= hours*3600;
        Long mins = runTime/60;
        runTime -= mins*60;
        Long secs = runTime;
        return String.format("%02d:%02d:%02d",hours, mins, secs );
    }
}
