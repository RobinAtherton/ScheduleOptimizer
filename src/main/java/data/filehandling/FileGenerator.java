package data.filehandling;

import util.helpers.own.logic.Formater;

import java.io.*;
import java.util.*;

class FileGenerator {


    //Reformat input
    static void reformat(String input, String output) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(input));
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        String line = br.readLine();
        while (line != null) {
            bw.write(line + ",#n");
            bw.newLine();
            bw.flush();
            line = br.readLine();
        }
    }
    //Filtered Methods
    static void writeFilteredSemesters(String output, File input, String... semesters) throws IOException {
        writeFiltered(output, input, semesters);
    }

    static void writeSemesterFilteredLessonCollections(String output, File input, String... semesters) throws IOException {
        writeFiltered(output, input, semesters);
    }

    public static void writeFilteredSemesterLessons(String output, File input, String... semesters) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        writeFilteredBlocks(reader, writer, semesters);
    }

    private static void writeFilteredBlocks(BufferedReader reader, BufferedWriter writer, String... semesters) throws IOException {
        List<String> lines = new ArrayList<>();
        List<String> result = new ArrayList<>();
        List<String> preResult = new ArrayList<>();
        int offset = 0;
        String line = reader.readLine();
        while(line != null) {
            String[] parts = Formater.clean(line);
            for (String semester : semesters) {
                if (parts[1].equals(semester)) {
                    lines.add(line);
                }
            }
            line = reader.readLine();
        }
        //0 = Id, 1 = semester, 2 = lecturer, 3 = subject, 4 = room, 5 = day, 6 = hour
        for (String outer : lines) {
            int MAX = Integer.MIN_VALUE;
            String block = outer;
            String[] outerParts = Formater.clean(outer);
            if (Integer.parseInt(outerParts[6]) > MAX) {
                MAX = Integer.parseInt(outerParts[6]);
            }

            for (String inner : lines) {
                String[] innerParts = Formater.clean(inner);
                if (Integer.parseInt(innerParts[0]) == Integer.parseInt(outerParts[0])
                        && innerParts[1].equals(outerParts[1])
                        && innerParts[2].equals(outerParts[2])
                        && innerParts[5].equals(outerParts[5])
                        && Integer.parseInt(innerParts[6]) < MAX) {
                    MAX = Integer.parseInt(innerParts[6]);
                    block = inner;
                }
            }
            preResult.add(block);
            result.add(block + "," + offset);
            offset++;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (String s : result) {
            String[] sP = Formater.clean(s);
            int blocklength = 1;
            for (String x : result) {
                String[] xP = Formater.clean(x);
                if (!(s.equals(x)) && sP[0].equals(xP[0]) && sP[5].equals(xP[5])) {
                    blocklength++;
                }
            }
            map.put(Integer.parseInt(sP[0]), blocklength);
        }

        Set<String> mySet = new HashSet<String>(preResult);
        Set<String> set2 = new HashSet<>();
        for (String s : mySet) {
            String[] split = Formater.clean(s);
            String x = map.get(Integer.parseInt(split[0])).toString();
            set2.add(s + "," + x);
        }

        for (String s : set2) {
            writer.write(s);
            writer.newLine();
            writer.flush();
        }
    }

    static void writeFilteredUKWLessons(String output, File input, String... semesters) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            for (String semester : semesters) {
                if (line.contains("uKW") && parts[4].equals(semester)) {
                    writer.write(parts[0] + "," + parts[4] + "," + parts[5] + "," + parts[6] + "," + parts[7] + ",#n");
                    writer.write("\n");
                    writer.flush();
                }
            }
            line = reader.readLine();
        }
    }

    static void writeFilteredGKWLessons(String output, File input, String... semesters) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            for (String semester : semesters) {
                if (line.contains("gKW") && parts[4].equals(semester)) {
                    writer.write(parts[0] + "," + parts[4] + "," + parts[5] + "," + parts[6] + "," + parts[7] + ",#n");
                    writer.write("\n");
                    writer.flush();
                }
            }
            line = reader.readLine();
        }

    }

    static void writeFilteredGroupedLessons(String output, File input, String... semesters) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            for (String semester : semesters) {
                if (line.contains("Gruppe") && parts[4].equals(semester)) {
                    writer.write(line + ",#n");
                    writer.write("\n");
                    writer.flush();
                }
            }
            line = reader.readLine();
        }
    }

    static void writeFilteredAltLessons(String output, File input, String... semesters) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            for (String semester : semesters) {
                if (!parts[11].equals(null) && !parts[11].equals("") && !parts[11].isEmpty()
                        && parts[4].equals(semester)) {
                    writer.write(line + ",#n");
                    writer.write("\n");
                    writer.flush();
                }
            }
            line = reader.readLine();
        }
    }

    static void writeFilteredFWPMLessons(String output, File input, String... semesters) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            for (String semester : semesters) {
                if (parts[4].equals(semester)) {
                    writeFWPMHelp(writer, line, parts);
                }
            }
            line = reader.readLine();
        }
    }

    private static void writeFWPMHelp(BufferedWriter writer, String line, String[] parts) throws IOException {
        for (String part : parts) {
            if (part.equals("FWPM")) {
                writer.write(line + ",#n");
                writer.newLine();
                writer.flush();
                break;
            }
        }
    }

    private static void writeFiltered(String output, File input, String[] semesters) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        iterateFile(reader, writer, line, semesters);
    }

    private static void iterateFile(BufferedReader reader, BufferedWriter writer, String line, String[] semesters) throws IOException {
        while (line != null) {
            for (String semester : semesters) {
                if (line.contains(semester)) {
                    writer.write(line + ",#n");
                    writer.write("\n");
                    writer.flush();
                }
            }
            line = reader.readLine();
        }
    }

    //Modfied Methods
    static void writeModifiedUKWLessons(String output, File input) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            if (line.contains("uKW")) {
                writer.write(parts[0] + "," + parts[4] + "," + parts[5] + "," + parts[6] + "," + parts[7] + ",#n");
                writer.write("\n");
                writer.flush();
            }
            line = reader.readLine();
        }
    }

    static void writeModifiedGKWLessons(String output, File input) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            if (line.contains("gKW")) {
                writer.write(parts[0] + "," + parts[4] + "," + parts[5] + "," + parts[6] + "," + parts[7] + ",#n");
                writer.write("\n");
                writer.flush();
            }
            line = reader.readLine();
        }
    }

    static void writeModifiedGroupedLessons(String output, File input) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            if (line.contains("Gruppe")) {
                writer.write(line + ",#n");
                writer.write("\n");
                writer.flush();
            }
            line = reader.readLine();
        }
    }

    static void writeModifiedAltLessons(String output, File input) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            if (!parts[10].equals(null) && !parts[10].equals("") && !parts[10].isEmpty()) {
                writer.write(line + ",#n");
                writer.newLine();
                writer.flush();
            }
            line = reader.readLine();
        }
    }

    static void writeModifiedFWPMLessons(String output, File input) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        String line = reader.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            writeFWPMHelp(writer, line, parts);
            line = reader.readLine();
        }
    }

}
