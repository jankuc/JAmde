/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.estimator.EstimatorBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.plaf.metal.MetalIconFactory;

public class RawTable {
    private Table table;

    public RawTable(Table table) {
        this.table = table;
    }

    /**
     * Creates the file if it doesn't exist; If it exists it returns the existing file.
     * 
     * @param tableFileName 
     */
    public File createFileIn(File parentFolder, String nameOfFile) {
        File file = new File(parentFolder.getAbsoluteFile() + File.separator + nameOfFile); // parent folder for all our folders
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
    
    public void printRaw(String directoryFileName) throws IOException {
        File parentDirectory = createFileIn(new File("/"), directoryFileName);
        
        ArrayList<TableInput> tableInputs = table.getTableInputs();
        ArrayList<TableRawOutput> tableRawOutputs = table.getTableRawOutputs();
        
        String tableFileName;
        TableRawOutput rawOutput;
        
        for (TableInput input : tableInputs) {
            File mixtureF = createFileIn(parentDirectory, input.getDistributionsString());
                       
            int index = tableInputs.indexOf(input);
            rawOutput = tableRawOutputs.get(index);

            double eps = input.getContamination();
            File epsF = createFileIn(mixtureF, String.format("eps=%.2f", eps));
            
            int K = input.getSizeOfEstimator();

            for (int n : input.getSizeOfSample()) {
                File nF = createFileIn(epsF, "n=" + n);
                
                for (EstimatorBuilder est : input.getEstimators()) {
                    // Renyi=0.3_K=1000_n=500_eps=0.3_N01-N010
                    tableFileName = "" + est.getType() + "=" + est.getPar() + 
                            "_K=" + input.getSizeOfEstimator() +  "_" +
                            nF.getName() + "_" + epsF.getName() + "_" + mixtureF.getName();
                    File estF = new File (nF +File.separator+ tableFileName); // estimator File
                    
                    FileWriter w = new FileWriter(estF);
                    
                    
                    
                    w.close();
                }
            }
        }
    }
    
}
