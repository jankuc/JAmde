/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

import jamde.table.Table;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kucerj28@fjfi.cvut.cz
 */
public class Main {

    /**
     * Depending on the command line arguments starts the JAmde with appropriate input and parameters. 
     * 
     * Example: JAmde file ./pathToFile/file 12 ./pathToTable/table.tex
     *          JAmde file ./pathToFile/file
     *          JAmde app --TODO
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        long timeStart = System.currentTimeMillis();
        
        String nameOfFile = "initializedValue";
        Table table = new Table();
        
        if (args.length == 0) {
            System.out.println("Not enough arguments.");
            return;
        }
        
        if (args[0].equals("file")) { // args[0] should be one of {file,app}
            try {
                nameOfFile = args[1];
            } catch(java.lang.ArrayIndexOutOfBoundsException e) {
                System.out.println("You did not specify name of the file, you want to load. Program is terminating.");
                return;
            }
        } else {
            System.out.println("Other than file-input not supported yet. Program is terminating.");
            return;
        }
             
        File confFile = new File(nameOfFile);
        
        if (! confFile.exists()) {
            System.out.println("File you chose does not exist. Program is terminating.");
            return;
        } else {
            try {
                table.loadInputsFromFile(confFile);
                System.out.println("Table input was succesfully loaded from the file.");
            } catch (Exception ex) {
                System.out.println("File you chose does not contain what it should. Program is terminating.");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // Now we have loaded the tableInput in Table from the file
        
        int numOfThreads = 25; // Default value
        try {
            numOfThreads = Integer.parseInt(args[2]);
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            System.out.println("You did not specify number of Threads you want to use. Default value is 25.");
        }
        
        numOfThreads = Math.min(numOfThreads, 30); // Designed for 32-core vkstat. So it doesn't take up all the cores.
        
        // the enumeration itself
        int countOutput;
        countOutput =  table.count(numOfThreads);
        if (countOutput == 1){ // Only the distanceTable was printed to the file
          return;
        }
        
        String tableFileName = System.getProperty("user.home") + "/tables/default/defaultTable.tex"; // creates absolute path to the file
        try { // did we specify the output file name when we started the program?
            tableFileName = args[3];
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            System.out.println("You did not specify name and path to the output file. It will be created for you. In ~/tables/default/defaultTable.tex");
        }
        
        
        File tableFile = new File(tableFileName);
        String newTableFileName = tableFileName;
        int numOfExistingFiles = 0;
        
        // we append number if the output file already exists
        while (tableFile.exists()) { // we change only newTableFileName
            numOfExistingFiles ++;
            newTableFileName = tableFileName.replaceFirst(".tex",  "" + numOfExistingFiles + ".tex");
            tableFile = new File(newTableFileName);
        }
        tableFileName = newTableFileName;
        
        table.printClassic(tableFileName);
        System.out.println("Result is saved in " + tableFileName);
        
        // pdflatex creation of .pdf of the table
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("pdflatex -output-directory " + tableFile.getParent() + " " + tableFile.getAbsolutePath());
        
        // copies output of pdflatex process to output of main.java
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while((line=input.readLine()) != null) {
          System.out.println(line);
        }
        
        // deletes .log and .aux files of pdflatex 
        File markedForRemoval = new File(tableFileName.replaceFirst(".tex", ".log"));
        markedForRemoval.delete();
        markedForRemoval = new File(tableFileName.replaceFirst(".tex", ".aux"));
        markedForRemoval.delete();
        
        // stops time 
        Long timeEnd = System.currentTimeMillis();
        Long runTime = timeEnd - timeStart;
        System.out.println("Runtime = " + MathUtil.Long2time(runTime) + ".");
        
        // if the program runs under user honza (it's on local machine, not on vkstat), it runs pdfviewer
        String username = System.getProperty("user.name");
        if (username.equals("honza")){ // we don't want to start evince on vkstat (login there is kucerj28)
            Process pr1 = rt.exec("evince " + tableFile.getAbsolutePath().replace("tex", "pdf"));
        }

    }
}
