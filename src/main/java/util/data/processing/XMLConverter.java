package util.data.processing;

import util.helpers.own.logic.Formater;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLConverter {

    public static File convert(File input, String output) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(input));
        BufferedWriter wr = new BufferedWriter(new FileWriter(output));
        String line = br.readLine();
        while (line != null) {
            if (line.contains("<Row>")) {
                String temp = "";
                line = br.readLine();
                while (!line.contains("</Row>")) {
                    String parts[] = line.split(">");
                    temp += parts[2];
                    temp += ",";
                    line = br.readLine();
                }
                wr.flush();
                temp = cleanUp(temp);
                if (temp.startsWith("FWPM")) {
                    wr.write(temp);
                    wr.newLine();
                }
            }
            line = br.readLine();
        }
        return new File(output);
    }

    public static File assertShortNames(File input, File comparison, String output) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(input));
        BufferedReader cp = new BufferedReader(new FileReader(comparison));
        BufferedWriter wr = new BufferedWriter(new FileWriter(output));
        List<String> compList = new ArrayList<>();
        String cLine = cp.readLine();
        //Reads all Strings from Subjects File
        while (cLine != null) {
            compList.add(cLine);
            cLine = cp.readLine();
        }
        String line = br.readLine();
        //For each line iterates through subjects
        while (line != null) {
            //Iterate through Subject Strings
            for (String x : compList) {
                //Cleaned up separate Parts of Subject Strings
                String[] parts = Formater.clean(x);
                //Separate parts of Occupancy
                String[] lineParts = line.split(",");
                //If the Occupancy contains the Subject
                if (lineParts[2].toLowerCase().contains(parts[1].toLowerCase()) && !parts[1].isEmpty() && parts[1] != "") {
                    write(wr, line, parts);
                    break;
                }
            }
            line = br.readLine();
        }
        return new File(output);
    }

    private static void write(BufferedWriter wr, String line, String[] parts) throws IOException {
        line = parts[0] + "," + line;
        wr.write(line);
        wr.newLine();
        wr.flush();
        return;
    }

    private static String cleanUp(String line) {
        line = cleanEntries(line);
        line = line.replace("Einführung in die Methoden der", " ");
        line = line.replace("Einführung in die", " ");
        line = line.replace("künstlichen", "künstliche");
        line = line.replace("(englischsprachig)", " ");
        while (line.contains("  ")) {
            line = line.replace("  ", " ");
        }
        while (line.contains(",,")) {
            line = line.replace(",,", ",");
        }
        line = line.replace(", ", ",");
        line = line.replace(" ,", ",");
        return line;
    }

    private static String cleanEntries(String line) {
        line = line.replace("</Data", "#i#");
        String[] split = line.split("#i#");
        split[0] = split[0].replace("-", " ");
        line = "";
        for (String x : split) {
            line +=x;
        }
        while (line.contains("  ")) {
            line = line.replace("  ", " ");
        }

        String parts[] = line.split(" ");
        String temp = "";
        parts[0] = parts[0].replace("FWP", "FWPM,");
        parts[1] = parts[1].replace("IF", "IF,");
        for (String x : parts) {
            temp += x;
            temp += " ";
        }
        return temp;
    }

}
