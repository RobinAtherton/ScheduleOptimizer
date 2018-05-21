package util.data.output;

import app.ScheduleSolution;
import domain.Lesson;
import util.data.processing.ScheduleProcessor;
import util.helpers.own.container.LessonContainer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleOutput {

    private static String kek = "---------------------------------------------------";
    //Broken
    public static void printScheduleToFile(ScheduleSolution scheduleSolution, File file) throws Exception {
        Map<String, List<Lesson>> schedule = ScheduleProcessor.sortLessonsToMap(scheduleSolution);
        Map<String, LessonContainer> scheduleByDays = ScheduleProcessor.sortBySemester(schedule);
        List<String> cachedSemesters = new ArrayList<>();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String semester : scheduleByDays.keySet()) {
            LessonContainer dC = scheduleByDays.get(semester);
        }
        for (String s : cachedSemesters) {
            bw.write(s);
            bw.newLine();
            bw.flush();
        }
    }

    public static void printLessonsBySemester(ScheduleSolution scheduleSolution, File file) throws Exception {
        Map<String, List<Lesson>> schedule = ScheduleProcessor.sortLessonsToMap(scheduleSolution);
        Map<String, LessonContainer> scheduleByDays = ScheduleProcessor.sortBySemester(schedule);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String semester : scheduleByDays.keySet()) {
            LessonContainer lC = scheduleByDays.get(semester);
            if (lC.isEmpty()) {
                bw.write("No schedule for" + semester);
                bw.newLine();
                bw.flush();
            } else {
                bw.write(kek  + semester + kek);
                bw.newLine();
                bw.write(lC.toString());
                bw.flush();
            }
        }
    }

}
