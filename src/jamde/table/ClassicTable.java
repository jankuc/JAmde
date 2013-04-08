/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.OtherUtils;
import jamde.estimator.Estimator;
import jamde.estimator.EstimatorBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author honza
 */
public class ClassicTable {
    private Table table;
    
    public ClassicTable(Table table){
        this.table = table;
    }
    
    /**
     * Encapsulates all the printing procedures for the "Classic" table. 
     * 
     * @param fileName
     * @throws IOException
     */
    public void printClassic(String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        printClassicHeadTex(file);
        ArrayList<TableInput> tableInputs = table.getTableInputs();
        ArrayList<TableOutput> tableOutputs = table.getTableOutputs();
        for (TableInput input : tableInputs) {
            printClassicHeadTable(file, input);
            int index = tableInputs.indexOf(input);
            TableOutput output = tableOutputs.get(index);
            for (EstimatorBuilder estBuilder : input.getEstimators()) {
                printClassicLine(file, input, output, estBuilder);
            }
            printClassicEndTable(file, input);
        }
        printClassicEndTex(file);
    } // END printClassic(String fileName)

    /**
     * Prints header of the .tex file for the Classic tables. Not the head of any table. <br>
     * Output: <br>
     * \documentclass[11pt]{article} <br>
     * &#92;usepackage[utf8]{inputenc} <br>
     * &#92;usepackage[czech]{babel} <br>
     * &#92;usepackage[landscape]{geometry} <br>
     * &#92;usepackage{pdflscape} <br>
     * \textwidth 200mm \textheight 205mm \oddsidemargin -5mm <br>
     * \evensidemargin 3mm \topmargin -25mm <br>
     * \newlength{\defbaselineskip} <br>
     * \setlength{\defbaselineskip}{\baselineskip} <br>
     * \newcommand{\setlinespacing}[1] <br>
     *  &#32;&#32;&#32;&#32;&#32;{\setlength{\baselineskip}{#1 \defbaselineskip}} <br>
     * \pagestyle{empty} <br>
     * \begin{document} <br>
     * 
     * @param file
     * @throws IOException
     */
    public void printClassicHeadTex(File file) throws IOException {
        FileWriter w = new FileWriter(file);
        w.write("\\documentclass[11pt]{article}\n");
        w.write("\\usepackage[utf8]{inputenc}\n");
        w.write("\\usepackage[czech]{babel}\n");
        w.write("\\usepackage[landscape]{geometry}\n");
        w.write("\\usepackage{pdflscape}\n");
        w.write("\\textwidth 200mm \\textheight 205mm \\oddsidemargin -5mm\n");
        w.write("\\evensidemargin 3mm \\topmargin -25mm\n");
        w.write("\\newlength{\\defbaselineskip}\n");
        w.write("\\setlength{\\defbaselineskip}{\\baselineskip}\n");
        w.write("\\newcommand{\\setlinespacing}[1]\n");
        w.write("   {\\setlength{\\baselineskip}{#1 \\defbaselineskip}}\n");
        w.write("\\pagestyle{empty}\n");
        w.write("\\begin{document}\n");
        w.close();
    }

    /**
     * Prints the end of the .tex file. <br>
     * Output:<br>
     * \end{document}<br>
     * 
     * @param file
     * @throws IOException
     */
    public void printClassicEndTex(File file) throws IOException {
        FileWriter w = new FileWriter(file, true); // so it appends
        w.write("\\end{document}\n");
        w.close();
    }

    /**
     * Depending on the number of <b>sizeOfSamples</b> in <b>input</b> produces 
     * "Classic" table Header. <br>
     * Output:<br>
     * \hline <br>
     * $\alpha\backslash n$ &&  $20$ & \\ <br>
     * \hline <br>
     * & $m(\mu)$ & $s(\mu)$ & $eref(\mu)$ \\<br>
     * & $m(\sigma)$ & $s(\sigma)$ & $eref(\sigma)$ \\ <br>
     * \hline <br>
     * 
     * -------------------------------------~ <br>
     * | a\n | &#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;
     * 20   
     * &#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32; |<br>
     * -------------------------------------~<br>
     * | &#32;&#32;&#32;&#32;&#32;    | &#32;m(u)&#32;  s(u) &#32; eref(u) &#32; | <br>
     * | &#32;&#32;&#32;&#32;&#32;    |&#32; m(s) &#32; s(s) &#32; eref(s) &#32; |<br>
     * -------------------------------------~<br>
     * 
     * @param file
     * @param input
     * @throws IOException
     */
    public void printClassicHeadTable(File file, TableInput input) throws IOException {
        Integer[] sizeOfSample = input.getSizeOfSample().toArray(new Integer[0]);
        FileWriter w = new FileWriter(file, true); // so it appends
        w.write("\\begin{table}[ht] \\footnotesize \n");
        w.write("\\begin{center} \n");
        w.write("\\begin{tabular}{|c|");
        String p1 = "\\mu",p2 = "\\sigma",p3 = "";
        if (input.getContaminated().toString().equals("Normal")){
            p1 = "\\mu";
            p2 = "\\sigma";
        } else if (input.getContaminated().toString().equals("Cauchy")){
            p1 = "\\mu";
            p2 = "\\sigma";
        } else if (input.getContaminated().toString().equals("Laplace")){
            p1 = "\\mu";
            p2 = "\\lambda";
        } else if (input.getContaminated().toString().equals("Exponential")){
            p1 = "\\mu";
            p2 = "\\lambda";
        } else if (input.getContaminated().toString().equals("Weibull")){
            p1 = "\\mu";
            p2 = "\\lambda";
            p3 = "k";
        }  
                
        for (int i : sizeOfSample) {
            w.write("ccc|");
        }
        w.write("} \n");
        w.write("\\hline \n"); // the upper border line
        w.write("$\\mathrm{Estimator}\\backslash n$ &&  $" + sizeOfSample[0] + "$"); // line with sizes of sample
        for (int i = 1; i < sizeOfSample.length; i++) { // I really want to go through items {1 : end} not {0:end}
            w.write(" &&&  $" + sizeOfSample[i] + "$");
        }
        w.write(" & \\\\ \n"); // ending of line with sizes of sample
        w.write("\\hline \n"); // borderline
        if (input.getParamsCounted().equals("first") 
                || input.getParamsCounted().equals("both") 
                || input.getParamsCounted().equals("all")){ // line in the head of the parameter mu
            for (int i = 0; i < sizeOfSample.length; i++) {
                w.write("& $m("+p1+")$ & $s("+p1+")$ & $eref("+p1+")$ ");
            }
            w.write("\\\\ \n");
        }
        if (input.getParamsCounted().equals("second") 
                || input.getParamsCounted().equals("both") 
                || input.getParamsCounted().equals("all")) { // line in the head of the parameter sigma
            for (int i = 0; i < sizeOfSample.length; i++) {
                w.write("& $m("+p2+")$ & $s("+p2+")$ & $eref("+p2+")$ ");
            }
            w.write("\\\\ \n");
        }
        if (input.getParamsCounted().equals("all")) { // line in the head of the parameter k (only for Weibull)
            for (int i = 0; i < sizeOfSample.length; i++) {
                w.write("& $m("+p3+")$ & $s("+p3+")$ & $eref("+p3+")$ ");
            }
            w.write("\\\\ \n");
        }
        
        w.write("\\hline \n"); // border line below the head of the table.
        w.close();
    }

    /**
     * Prints the caption and other for the Classic table ending. 
     * 
     * @param file
     * @param input
     * @throws IOException
     */
    public void printClassicEndTable(File file, TableInput input) throws IOException {
        FileWriter ww = new FileWriter(file, true); // true: so it appends
        PrintWriter w = new PrintWriter(ww);
        w.write("\\end{tabular}\n");
        ArrayList<EstimatorBuilder> eBs = input.getEstimators();
        //double a,b;
        //a = input.getContaminated().getP1();
        //b = input.getContaminated().getP1();
        /*
         * TODO dodelat ruzne vypisy pro N(0,1), N(0,005)
         */
        String sContaminated;
        if (input.getContaminated().toString().equals("Weibull")) {
            sContaminated = String.format("\\mathrm{%c}(%s,%s,%s)",
                    input.getContaminated().toString().charAt(0),
                    OtherUtils.num2str(input.getContaminated().getP1()),
                    OtherUtils.num2str(input.getContaminated().getP2()),
                    OtherUtils.num2str(input.getContaminated().getP3()));
        } else {
            sContaminated = String.format("\\mathrm{%c}(%s,%s)",
                    input.getContaminated().toString().charAt(0),
                    OtherUtils.num2str(input.getContaminated().getP1()),
                    OtherUtils.num2str(input.getContaminated().getP2()));
        }
        w.write("\\caption{" + eBs.get(1).getType() + ": $p = " + sContaminated + "$, data: "); // caption creation       
        if (input.getContaminating() == null) {
            if (input.getPathToDataFile()== null) { // we need to make orderErrors caption
                ArrayList orderErrors = input.getOrderErrors();
                double prob = 1;
                double[] probs = new double[orderErrors.size()];
                double[] orders = new double[orderErrors.size()];
                String[] distrs = new String[orderErrors.size()];
                for (int i = 0; i < orderErrors.size(); i++) {
                    orders[i] = ((double[])orderErrors.get(i))[0];
                    probs[i] = ((double[])orderErrors.get(i))[1];
                    prob -= probs[i]; 
                    distrs[i] = String.format("%.1f\\mathrm{%c}_{%.1fx}(%s,%s)",
                            probs[i],
                            input.getContaminated().toString().charAt(0),
                            orders[i],
                            OtherUtils.num2str(input.getContaminated().getP1()),
                            OtherUtils.num2str(input.getContaminated().getP2()));
                }
                w.write("$" + prob + sContaminated);
                for (int i = 0; i < distrs.length; i++) {
                    w.write(" + " + distrs[i]);
                }
                w.write("$, $K = " + input.getSizeOfEstimator() + "$} \n");    
            } else { // data were loaded from a file
                w.write(" Data načtena ze souboru " + 
                        input.getPathToDataFile() +". \n");
            }
        } else { // data were created as a mixture of distributions
            String sContaminating;
            if (input.getContaminating().toString().equals("Weibull")) {
                sContaminating = String.format("\\mathrm{%c}(%s,%s,%s)",
                        input.getContaminating().toString().charAt(0),
                        OtherUtils.num2str(input.getContaminating().getP1()),
                        OtherUtils.num2str(input.getContaminating().getP2()),
                        OtherUtils.num2str(input.getContaminating().getP3()));
            } else {
                sContaminating = String.format("\\mathrm{%c}(%s,%s)",
                        input.getContaminating().toString().charAt(0),
                        OtherUtils.num2str(input.getContaminating().getP1()),
                        OtherUtils.num2str(input.getContaminating().getP2()));
            }
            w.write("$(1-\\varepsilon)" + sContaminated + " + \\varepsilon " +
                    sContaminating + "$, $\\varepsilon =  " + 
                    input.getContamination() + "$, $K = " + 
                    input.getSizeOfEstimator() + "$} \n");
        }
        w.write("\\end{center}\n");
        w.write("\\end{table}\n");
        w.close();
    }

    /**
     * Prints line with data into the "Classic" table. If the counted parameters are "both" then it prints two lines, one for every parameter.  
     * 
     * @param file
     * @param input
     * @param output
     * @param estimator
     * @throws IOException 
     */
    private void printClassicLine(File file, TableInput input, TableOutput output, EstimatorBuilder estimator) throws IOException {
        Estimator est = estimator.getEstimator();
        
        FileWriter ww = new FileWriter(file, true); // so it appends
        PrintWriter w = new PrintWriter(ww);
        if (input.getParamsCounted().equals("first") 
                || input.getParamsCounted().equals("both") 
                || input.getParamsCounted().equals("all")) { // printing of first parameter statistics
            w.write(est.getClassicTableName());
            for (int sizeOfSample : input.getSizeOfSample()) {
                w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $",
                        output.getMeanValue(estimator, sizeOfSample, 1),
                        output.getDeviation(estimator, sizeOfSample, 1),
                        output.getEfficiency(estimator, sizeOfSample, 1));
            }
            w.write("\\\\ \n");
        }
        if (input.getParamsCounted().equals("second") 
                || input.getParamsCounted().equals("both") 
                || input.getParamsCounted().equals("all")) {// printing of second parameter statistics
            if (input.getParamsCounted().equals("second")) {
                w.write(est.getClassicTableName());
                for (int sizeOfSample : input.getSizeOfSample()) {
                    w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $",
                            output.getMeanValue(estimator, sizeOfSample, 1),
                            output.getDeviation(estimator, sizeOfSample, 1),
                            output.getEfficiency(estimator, sizeOfSample, 1));
                }
                w.write("\\\\ \n");
            } else {
                for (int sizeOfSample : input.getSizeOfSample()) {
                    w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $",
                            output.getMeanValue(estimator, sizeOfSample, 2),
                            output.getDeviation(estimator, sizeOfSample, 2),
                            output.getEfficiency(estimator, sizeOfSample, 2));
                }
                w.write("\\\\ \n");
            }
        }
        if (input.getParamsCounted().equals("all")) { // printing of third parameter statistics
            for (int sizeOfSample : input.getSizeOfSample()) {
                w.format(" & $ %.3f $ & $ %.3f $ & $ %.3f $",
                        output.getMeanValue(estimator, sizeOfSample, 3),
                        output.getDeviation(estimator, sizeOfSample, 3),
                        output.getEfficiency(estimator, sizeOfSample, 3));
            }
            w.write("\\\\ \n");
        }
        w.write("\\hline \n");
        w.close();
    }
    
    
}
