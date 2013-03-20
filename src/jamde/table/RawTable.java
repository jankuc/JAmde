/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.Main;
import jamde.MatlabControl;
import jamde.estimator.EstimatorBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            File epsF = createFileIn(mixtureF, String.format("eps-%.2f", eps));
            
            int K = input.getSizeOfEstimator();

            for (int n : input.getSizeOfSample()) {
                File nF = createFileIn(epsF, "n-" + n);
                
                for (EstimatorBuilder est : input.getEstimators()) { 
                    // Renyi=0.3_K=1000_n=500_eps=0.3_N01-N010
                    tableFileName = "" + est.getType() + "=" + est.getPar() + 
                            "_K-" + input.getSizeOfEstimator() +  "_" +
                            nF.getName() + "_" + epsF.getName() + "_" + mixtureF.getName();
                    File estF = new File (nF +File.separator+ tableFileName); // estimator File
                    try (FileWriter ww = new FileWriter(estF); PrintWriter w = new PrintWriter(ww)) {
                        for (int i = 0; i < K; i++) {
                            if (input.getParamsCounted().equals("first") || input.getParamsCounted().equals("both") || input.getParamsCounted().equals("all")) {
                                w.write(String.format("%.4f  ", rawOutput.getEstimatedParameter(est, n, 1, i)));
                            }
                            if (input.getParamsCounted().equals("second") || input.getParamsCounted().equals("both") || input.getParamsCounted().equals("all") || input.getParamsCounted().equals("second&third")) { 
                                w.write(String.format(" %.4f  ", rawOutput.getEstimatedParameter(est, n, 2, i)));
                            }
                            if (input.getParamsCounted().equals("third") || input.getParamsCounted().equals("all") || input.getParamsCounted().equals("second&third")) {
                                w.write(String.format("%.4f  ", rawOutput.getEstimatedParameter(est, n, 3, i)));
                            }
                            w.write("\n");
                        }
                    } catch (FileNotFoundException e){
                        /*
                         * TODO catch the exception
                         */
                    }
                }
            }
        }
    }
    
    public void printDistanceFunctions(String directoryFileName) throws IOException {
        File parentDirectory = createFileIn(new File("/"), directoryFileName);
        
        ArrayList<TableInput> tableInputs = table.getTableInputs();
        ArrayList<TableRawOutput> tableRawOutputs = table.getTableRawOutputs();
        
        String distanceFileName;
        TableRawOutput rawOutput;
        
        for (TableInput input : tableInputs) {
            File mixtureF = createFileIn(parentDirectory, input.getDistributionsString());
                       
            int index = tableInputs.indexOf(input);
            rawOutput = tableRawOutputs.get(index);

            double eps = input.getContamination();
            File epsF = createFileIn(mixtureF, String.format("eps-%.2f", eps));
            
            int K = input.getSizeOfEstimator();

            for (int n : input.getSizeOfSample()) {
                File nF = createFileIn(epsF, "n-" + n);
                
                for (EstimatorBuilder est : input.getEstimators()) { 
                    // Renyi=0.3_K-1000_n-500_eps-0.3_N01-N010
                    distanceFileName = "" + est.getType() + "-" + est.getPar() + 
                            "_K-" + input.getSizeOfEstimator() +  "_" +
                            nF.getName() + "_" + epsF.getName() + "_" + mixtureF.getName() + ".dist";
                    File distanceF = new File (nF +File.separator+ distanceFileName); // Distance File
                    
                    table.printDistanceFunction(input, est.getEstimator(), n, distanceF);
                    
                    String cmdMatlab = "matlab -nodisplay -r ";
                    String cmdChangeDir = "cd " + nF.getAbsolutePath() + "; ";
                    String cmdFigure = "C = load(\'" + distanceF.getAbsolutePath()+"\'); f = figure(\'visible\',\'off\'); imagesc(C); colorbar; saveas(f,\'"+ distanceF.getAbsolutePath() +".png\',\'png\'); quit";
                    
                    
                    String cmdRunScript = "cd /home/honza/Documents/FJFI/Renyi/JAmde/; makeFig;";
                    
                    Main.executeWithOutput("./matlabiSkript.sh " + distanceF.getAbsolutePath());
                    
                    //MatlabControl mc = new MatlabControl();
                    //mc.eval( "A = [1 1; 1 1]; save(\'/home/honza/A.mat\',\'A\');quit;");
                    //mc.eval(cmdChangeDir);
                    //mc.eval(cmdFigure);
                    
                    //Process pOctave = new ProcessBuilder(cmdOctave).start();

                    
                    
                    
                }
            }
        }
    }
    
}
