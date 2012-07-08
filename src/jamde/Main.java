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
 * @author honza Kučera
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
        
        int numOfThreads = 12; // Default value
        try {
            numOfThreads = Integer.parseInt(args[2]);
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            System.out.println("You did not specify number of Threads you want to use. Default value is 12. For vkstat it is OK.");
        }
        
        numOfThreads = Math.min(numOfThreads, 30); // Designed for 32-core vkstat. So it doesn't take up all cores.
        
        int countOutput;
        countOutput =  table.count(numOfThreads);
        if (countOutput == 1){ // Only the distanceTable was printed to the file
          return;
        }
        
        String tableFileName = System.getProperty("user.home") + "/tables/default/defaultTable.tex"; // creates absolute path to the file
        try { // did we specify the output file name when we started the program?
            tableFileName = args[3];
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            System.out.println("You did not specify name and path to the output file. ");
        }
        
        File tableFile = new File(tableFileName);
        String newTableFileName = tableFileName;
        int numOfExistingFiles = 0;
        
        while (tableFile.exists()) { // we change only newTableFileName and 
            numOfExistingFiles ++;
            newTableFileName = tableFileName.replaceFirst(".tex",  "" + numOfExistingFiles + ".tex");
            tableFile = new File(newTableFileName);
        }
        tableFileName = newTableFileName;
        
        table.printClassic(tableFileName);
        System.out.println("Result is saved in " + tableFileName);
        
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("pdflatex -output-directory " + tableFile.getParent() + " " + tableFile.getAbsolutePath());
        // we delete .log and .aux
                
        String username = System.getProperty("user.name");
        if (username.equals("honza")){ // we don't want to start evince on vkstat (login there is kucerj28)
            Process pr1 = rt.exec("evince " + tableFile.getAbsolutePath().replace("tex", "pdf"));
        }
        
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while((line=input.readLine()) != null) {
          System.out.println(line);
        }
       
        File markedForRemoval = new File(tableFileName.replaceFirst(".tex", ".log"));
        markedForRemoval.delete();
        markedForRemoval = new File(tableFileName.replaceFirst(".tex", ".aux"));
        markedForRemoval.delete();
        
        //System.setProperty("user.dir", dir);
        //System.out.println(System.getProperty("user.dir"));
        
        //Process p = Runtime.getRuntime().exec("pdflatex " + tableFileName);
        
        Long timeEnd = System.currentTimeMillis();
        Long runTime = timeEnd - timeStart;
        System.out.println("Runtime = " + MathUtil.Long2time(runTime) + ".");

    }
}
