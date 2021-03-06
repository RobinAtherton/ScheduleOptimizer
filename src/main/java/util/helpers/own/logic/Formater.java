package util.helpers.own.logic;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Formater {

    public static String[] clean(String string) {
        String[] parts = string.split(",");
        cleanStrings(parts);
        return parts;
    }

    private static void cleanStrings(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].replace("\"", "");
        }
    }

    public static int count(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if(endsWithoutNewLine) {
                ++count;
            }
            return count;
        } finally {
            is.close();
        }
    }

}
