/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

import jamde.table.ClassicTable;
import jamde.table.RawTable;
import jamde.table.Table;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kucerj28@fjfi.cvut.cz
 */
public class Main {
    
    /**
     * Appends number to <b>fileName</b>, so the name is in it's destination unique. <br>
     * <br>
     * Example: if in desired direcory exist files [file.tex, file1.tex, file2.tex], it renames the current file to file3.tex.
     * 
     * @param fileName has to end with <b>.tex</b>
     * @return 
     */
    private static File MakeUniqueNamedTexFile(String fileName) {
        File file = new File(fileName);
        String newFilename;
        int numOfExistingFiles = 0;

        // we append number if the output file already exists
        while (file.exists()) { // we change only newTableFileName
            numOfExistingFiles++;
            newFilename = fileName.replaceFirst(".tex", "" + numOfExistingFiles + ".tex");   
            file = new File(newFilename);
        }
        return file;
    }

    /**
     * Appends number to <b>fileName</b>, so the name is in it's destination unique. <br>
     * <br>
     * Example: if in desired direcory exist files [file, file1, file2], it renames the current file to file3.
     * 
     * @param fileName
     * @return 
     */
    private static File MakeUniqueNamedFile(String tableFileName) {
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
    
    public static void executeWithOutput(String cmd) throws IOException {
        
            Process proc = Runtime.getRuntime().exec(cmd);
            // copies output of pdflatex process to output of main.java
            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while((line=input.readLine()) != null) {
                System.out.println(line);
            }
        
        
    }
    
    
    private static final class InputArgs{
        private boolean tableClassicBool = false;
        private boolean tableRawBool = false;
        private boolean printDistanceFunctionsBool = false;
        
        private String inputFile = "none";
        private int numOfThreads = 25;
        private String outputFile = System.getProperty("user.home") + "/tables/default/defaultTable";
        
        
        public InputArgs() {
        }

        public boolean isTableClassicBool() {
            return tableClassicBool;
        }

        public boolean isTableRawBool() {
            return tableRawBool;
        }

        public boolean isPrintDistanceFunctionsBool() {
            return printDistanceFunctionsBool;
        }
        
        public String getInputFile() {
            return inputFile;
        }

        public int getNumOfThreads() {
            return numOfThreads;
        }

        public String getOutputFile() {
            return outputFile;
        }
       
        
        /**
         * Reads command-Line arguments and translates them into right format.
         * 
         * @param args
         * @return 
         */
        public int setInputArgs(String[] args) {
            if (args.length == 0) {
                System.out.println("Not enough arguments.");
                
            }
            int i = 0;
            while (i < args.length) {

                switch (args[i]) { // CAUTION!: switch on Strings is suported only by source 7, not by source 6.
                    case "infile":
                        inputFile = args[i + 1];
                        break;
                    case "outfile":
                        outputFile = args[i + 1];
                        break;
                    case "threads":
                        numOfThreads = Integer.parseInt(args[i + 1]);
                        break;
                    case "print":
                        switch (args[i + 1]) {
                            case "raw":
                                tableRawBool = true;
                                break;
                            case "classic":
                                tableClassicBool = true;
                                break;
                            case "distances":
                                printDistanceFunctionsBool = true;
                            case "distance":
                                printDistanceFunctionsBool = true;
                        }
                }
                i = i + 2;
            }
            if (inputFile.equals("none")) {
                System.out.println("You did not specify name of the file, you want to load. Program is terminating.");
                return 1;
            }

            if (!(tableClassicBool || tableRawBool)) {
                System.out.println("You have not specified ANY output.\nDo this by \"table raw\" or \"table classic\" \n Terminating. ");
                return 1;
            }
            
            if ( outputFile.equals(System.getProperty("user.home") + "/tables/default/defaultTable")){
                System.out.println("You have not specify name and path to the output file");
            }
            return 0;
        }
    }

    /**
     * Depending on the command line arguments starts the JAmde with appropriate input and parameters. 
     * 
     * Example: java -jar JAmde infile ./pathToFile/file threads 12 outfile ./pathToTable/table print raw print classic print distances
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        
        InputArgs inputArgs = new InputArgs();
        
        if (inputArgs.setInputArgs(args)==1) {
            return;
        } 
        
        long timeStart = System.currentTimeMillis();      
       
        Table table = new Table();
        File inputFile = new File(inputArgs.getInputFile());
        
        if (! inputFile.exists()) {
            System.out.println("File you chose does not exist. Program is terminating.");
            return;
        } else {
            try {
                table.loadInputsFromFile(inputFile);
                System.out.println("Table input was succesfully loaded from the file.");
            } catch (Exception ex) {
                System.out.println("File you chose does not contain what it should. Program is terminating.");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // Now we have loaded the tableInput in Table from the file
        
        
        int numOfThreads = inputArgs.getNumOfThreads();
        numOfThreads = Math.min(numOfThreads, 30); // Designed for 32-core vkstat. So it doesn't take up all the cores.
        
        // the enumeration itself
        int countOutput;
        countOutput =  table.count(numOfThreads);
        if (countOutput == 1){ // Only the distanceTable was printed to the file
          return;
        }
        /**
         * TODO vytvorit funkci pro vykreslovani vzdalenostnich obrazku
         */
        
        // OUTPUT
        
        // List of *.tex files on which pdflatex will be called
        ArrayList<File> texFiles = new ArrayList<>();
        String tableFileName;
        
        // CLASSIC TABLE
        if (inputArgs.isTableClassicBool()) {
            tableFileName = inputArgs.getOutputFile().concat(".tex");
            
            File tableFile = MakeUniqueNamedTexFile(tableFileName);
            tableFileName =  tableFile.getAbsolutePath();

            ClassicTable classicTable = new ClassicTable(table);
            classicTable.printClassic(tableFileName);
            System.out.println("Result is saved in " + tableFileName);
            
            texFiles.add(tableFile);            
        }
        
        // RAW TABLE || DISTANCE FUNCTIONS
        if (inputArgs.isTableRawBool() || inputArgs.isPrintDistanceFunctionsBool()) {
            tableFileName = inputArgs.getOutputFile();
            
            File tableFile = MakeUniqueNamedFile(tableFileName);
            tableFileName =  tableFile.getAbsolutePath();
            
            RawTable rawTable = new RawTable(table);
            
            if (inputArgs.isTableRawBool()) {
                rawTable.printRaw(tableFileName);
            }
            if (inputArgs.isPrintDistanceFunctionsBool()) {
                rawTable.printDistanceFunctions(tableFileName);
            }
        }
        
        // pdfLatex on all .tex files
        Runtime rt = Runtime.getRuntime();
        for (File texFile : texFiles) {
            executeWithOutput("pdflatex -output-directory " + texFile.getParent() + " " + texFile.getAbsolutePath());
            
            rt.exec("evince " + texFile.getAbsolutePath().replaceAll("tex", "pdf"));
            // deletes .log and .aux files of pdflatex 
            File markedForRemoval = new File(texFile.getAbsolutePath().replaceFirst(".tex", ".log"));
            markedForRemoval.delete();
            markedForRemoval = new File(texFile.getAbsolutePath().replaceFirst(".tex", ".aux"));
            markedForRemoval.delete();
        }
        System.out.println("Result is saved.");
       
        // stops time 
        Long timeEnd = System.currentTimeMillis();
        Long runTime = timeEnd - timeStart;
        System.out.println("Runtime = " + MathUtil.Long2time(runTime) + ".");
        
        // if the program runs under user honza (it's on local machine, not on vkstat), it runs pdfviewer
//        String username = System.getProperty("user.name");
//        if (username.equals("honza")){ // we don't want to start evince on vkstat (login there is kucerj28)
//            Process pr1 = rt.exec("evince " + tableFileName.getAbsolutePath().replace("tex", "pdf"));
//        }

    }
}
