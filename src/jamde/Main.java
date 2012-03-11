/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

import jamde.table.Table;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author honza Kučera
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
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
        
        table.count();
        table.printClassic("/home/honza/Downloads/pokusnaTabulka.tex");
        Runtime.getRuntime().exec("pdflatex /home/honza/Downloads/pokusnaTabulka.tex");
        
        
        
        
        
    }
}
