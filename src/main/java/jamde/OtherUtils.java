/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

import java.io.File;

/**
 *
 * @author honza
 */
public class OtherUtils {
    
    /**
     * Appends number to <b>fileName</b>, so the name is in it's destination unique. <br>
     * <br>
     * Example: if in desired direcory exist files [file, file1, file2], it renames the current file to file3.
     * 
     * @param fileName
     * @return 
     */
    public static File MakeUniqueNamedFile(String tableFileName) {
        File tableFile = new File(tableFileName);
        String newTableFileName;
        int numOfExistingFiles = 0;

        // we append number if the output file already exists
        while (tableFile.exists()) { // we change only newTableFileName
            newTableFileName = tableFileName.concat(".hurdyHurdy");
            numOfExistingFiles++;
            newTableFileName = newTableFileName.replaceFirst(".hurdyHurdy", "" + numOfExistingFiles + ".hurdyHurdy");
            newTableFileName = newTableFileName.replaceFirst(".hurdyHurdy", "");
            tableFile = new File(newTableFileName);
        }
        return tableFile;
    }
    
    /**
     * Returns String representation of double d. Function truncates all the zeroes from the String.
     * 
     * Example:
     *      d = 123.456 -> "123.456" 
     *      d = 123.400 -> "123.4" 
     *      d = 123.00 -> "123" 
     * 
     * @param d... number of which we want string representation 
     * @return String 
     */
    public static String num2str(double d) {
        long factor = 1;
        double eps = 0.000001;
        double nasobeny = (int) ((int) factor * d);
        if (Math.abs(nasobeny / factor - d) < eps) {
            return String.format("%d", (int) d);
        }
        factor *= 10;
        nasobeny = (int) ((int) factor * d);
        if (Math.abs(nasobeny / factor - d) < eps) {
            return String.format("%.1f", d);
        }
        factor *= 10;
        nasobeny = (int) ((int) factor * d);
        if (Math.abs(nasobeny / factor - d) < eps) {
            return String.format("%.2f", d);
        }
        factor *= 10;
        nasobeny = (int) ((int) factor * d);
        if (Math.abs(nasobeny / factor - d) < eps) {
            return String.format("%.3f", d);
        }
        factor *= 10;
        nasobeny = (int) ((int) factor * d);
        if (Math.abs(nasobeny / factor - d) < eps) {
            return String.format("%.4f", d);
        }
        return String.format("%.5f", d);
    }
}
