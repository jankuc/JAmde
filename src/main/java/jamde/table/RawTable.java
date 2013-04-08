/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.Main;
import jamde.estimator.EstimatorBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RawTable {
    private Table table;

    public RawTable(Table table) {
        this.table = table;
    }

    private final class PrintDistanceFile extends Thread{
        TableInput input;
        EstimatorBuilder estB;
        int n;
        File distanceFile;

        public PrintDistanceFile() {
        }

        public PrintDistanceFile(TableInput input, EstimatorBuilder estB, int n, File distanceFile) {
            setPrintDistanceFile(input, estB, n, distanceFile);
        }
        
        public void  setPrintDistanceFile(TableInput input, EstimatorBuilder estB, int n, File distanceFile) {
            this.input = input;
            this.estB = estB;
            this.n = n;
            this.distanceFile = distanceFile;
        }
        
        @Override
        public void run() {
            table.printDistanceFunction(input, estB.getEstimator(), n, distanceFile);
        }
        
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
    
    /**
     * Prints into separate files the results of all the K estimators. Always all the estimated parameters. 
     * 
     * It creates a directory tree, which starts in <b> directoryFileName <\b>
     * 
     * @param directoryFileName...Root directory for the whole directory tree.
     * @throws IOException 
     */
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
    
    /**
     * Prints into separate files the function values of the minimized distance functions on <-4,4>x<0.001,8>. 
     * This is done by creating and executing 20 PrintDistanceFile threads.
     * Files are created in the same directory structure as RawFiles.
     * It also creates file in JAmde root directory, which contains all the paths
     * to these distanceFiles and then calls MATLAB to draw these pictures.
     * 
     * It creates a directory tree, which starts in <b> directoryFileName <\b>
     * 
     * @param directoryFileName...Root directory for the whole directory tree.
     * @throws IOException
     * @throws InterruptedException 
     */
    public void printDistanceFunctions(String directoryFileName) throws IOException, InterruptedException {
        File parentDirectory = createFileIn(new File("/"), directoryFileName);
        
        ArrayList<TableInput> tableInputs = table.getTableInputs();
        ArrayList<TableRawOutput> tableRawOutputs = table.getTableRawOutputs();
        
        String distanceFileName;
        TableRawOutput rawOutput;
        
        ArrayList<String> pathsToDistanceFiles = new ArrayList<>();
        
        int i = 1, j;
        int numOfThreads = 20; // writeOut of distacnceFiles is made by 20 threads
        PrintDistanceFile[] threadArray = new PrintDistanceFile[numOfThreads];
        
        for (int k = 0; k < threadArray.length; k++) {
            threadArray[k] = new PrintDistanceFile();
        }
        
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
                    distanceFileName = "" + est.getType() + "-" + est.getPar() + 
                            "_K-" + input.getSizeOfEstimator() +  "_" +
                            nF.getName() + "_" + epsF.getName() + "_" + mixtureF.getName() + ".dist";
                    // Result lokks like Renyi-[0.1]_K-10_n-2_eps-0.10_N0.1-N0.10.dist
                    distanceFileName = distanceFileName.replaceAll(" ", "");
                    File distanceFile = new File (nF +File.separator+ distanceFileName); // Distance File

                    j = i % numOfThreads; // Ensures, that the cycle rotates all "numOfThreads" threads
                    threadArray[j].join(); // waits for the current thread to finish writing into the previous distanceFile
                    threadArray[j] = new PrintDistanceFile(input, est, n, distanceFile); // initializes configuration for printing the file
                    threadArray[j].start(); // prints the file
                    i = i + 1;

                    pathsToDistanceFiles.add(distanceFile.getAbsolutePath()); // adds the file to the list later loaded by matlab for drawing.
                }
            }
        }
        
        for (int k = 0; k < threadArray.length; k++) {
            if(threadArray[k].isAlive()){
                threadArray[k].join();
            }   
        }
        
        File pathsFile = new File("pathsToDistanceFiles");
        printPathsToFile(pathsFile, pathsToDistanceFiles);
        Main.executeWithOutput("./script_drawDistances.sh " + pathsFile.getAbsolutePath());
    }

    private void printPathsToFile(File file, ArrayList<String> pathsToDistanceFiles) throws IOException {
        FileWriter fw = new FileWriter(file);
        if (file.exists()) {
            for (String path:pathsToDistanceFiles) {
                fw.write(path + " \n");
            }
            fw.write("\n");
            fw.close();
        }
    }
    
}
