package data.output;

import domain.Lesson;
import util.helpers.own.logic.Formater;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UntisExporter {

    public static void export(List<Lesson> lessons, String input, String output) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(input));
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        List<String> lines = new ArrayList<>();
        List<Lesson> contained = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        for (Lesson lesson : lessons) {
            for (String inner : lines) {
                String[] split = Formater.clean(inner);
                if (lesson.getId() == Integer.parseInt(split[0])
                        && lesson.getSemesterName().equals(split[1])
                        && lesson.getSubjectName().equals(split[3])
                        && !contained.contains(lesson)) {
                    contained.add(lesson);
                    for (int i = 0; i < lesson.getBlockLength(); i++) {
                        bw.write(format(lesson, i));
                        bw.newLine();
                        bw.flush();
                    }
                }
            }
        }
    }

    private static String format(Lesson lesson, int counter) {
        int hour = lesson.getHour() + counter;
        return lesson.getId() + ","
                + lesson.getSemesterName() + ","
                + lesson.getLecturerName() + ","
                + lesson.getSubjectName() + ","
                + lesson.getRoomName() + ","
                + lesson.getDay() + ","
                + hour;
    }
}
