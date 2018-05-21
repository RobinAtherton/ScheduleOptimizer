package util.fileHandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileAnalyzer {

    public static int analyzeRows(String pathname) throws IOException {
        int rows = 0;
        BufferedReader reader = new BufferedReader(new FileReader(pathname));
        String line = reader.readLine();
        while (line != null) {
            rows++;
            line = reader.readLine();
        }
        return rows;
    }

    public static int analyzeColumns(String pathname) throws IOException {
        int columns = 0;
        int max = Integer.MIN_VALUE;
        BufferedReader reader = new BufferedReader(new FileReader(pathname));
        String line = reader.readLine();
        while (line != null) {
            char[] chars = line.toCharArray();
            for (char c : chars) {
                columns++;
            }
            if (columns > max) {
                max = columns;
            }
            line = reader.readLine();
        }
        return max;
    }
}

