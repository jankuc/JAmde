/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
//import matlabcontrol.*;

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
    
    public static void executeWithOutput(String cmd) throws IOException {    
            Process proc = Runtime.getRuntime().exec(cmd);
            // copies output of pdflatex process to output of main.java
            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while((line=input.readLine()) != null) {
                System.out.println(line);
            }
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
    
    /**
     * Sends mail notifying of the end of the computation.
     * 
     * @param to ... email address of the recipient
     */
    public static void sendMail(String to) {
 
        final String username = "jamde.gams@gmail.com";
        final String password = "OdhadujmeSCitem";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("jamde.gams@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("JAmde doběhlo");
            message.setText("Milý uživateli, \n\nprávě jsem skončil výpočet, "
                    + "takže můžeš dát vkstatu další nálož. \n\n"
                    + "S pozdravem, tvůj milující \n\n"
                    + "JAmde ");

            Transport.send(message);

            System.out.println("Mail sent.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    
    /**
     * Runs pdflatex on all .tex files. If name of the machine contains "jethro",
     * runs evince, which displays the .pdf files.
     * 
     * @param texFiles
     * @throws IOException 
     */
    static void pdfLatex(ArrayList<File> texFiles) throws IOException {
        for (File texFile : texFiles) {
            runCommand("pdflatex -output-directory " + texFile.getParent() + " " + texFile.getAbsolutePath());
            
            if (InetAddress.getLocalHost().getHostName().contains("jethro")) {
                Runtime.getRuntime().exec("evince " + texFile.getAbsolutePath().replaceAll("tex", "pdf"));
            }
                
            // deletes .log and .aux files of pdflatex 
            File markedForRemoval = new File(texFile.getAbsolutePath().replaceFirst(".tex", ".log"));
            markedForRemoval.delete();
            markedForRemoval = new File(texFile.getAbsolutePath().replaceFirst(".tex", ".aux"));
            markedForRemoval.delete();
        }
        System.out.println("Result is saved.");
    }
    
    public static void runCommand(String arg) {

        String s = null;

        try {
            Process p = Runtime.getRuntime().exec(arg);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("OtherUtils.runCommand.stdOut:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("OtherUtils.runCommand.stdErr:\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
        }
    }
    
    public static void runMatlabCommand(String cmd) throws MatlabConnectionException, MatlabInvocationException {
        //Create a proxy, which we will use to control MATLAB
        
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                                         .setHidden(true)
                                         .build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        MatlabProxy proxy = factory.getProxy();
        
        //Display 'hello world' just like when using the demo
        proxy.eval(cmd);

        //Disconnect the proxy from MATLAB
        proxy.disconnect();
    }
    
}
