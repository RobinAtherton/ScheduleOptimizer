package util.data.processing;

import app.ScheduleSolution;
import domain.Lesson;
import domain.Semester;
import util.helpers.own.container.LessonContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleProcessor {

    public static Map<String, LessonContainer> sortBySemester(Map<String, List<Lesson>> schedule) throws Exception {
        Map<String, LessonContainer> container = new HashMap<>();
        for (String semester : schedule.keySet()) {
            LessonContainer containerDummy = new LessonContainer();
            List<Lesson> lessons = schedule.get(semester);
            for (Lesson lesson : lessons) {
                containerDummy.add(lesson);
            }
            container.put(semester, containerDummy);
        }
        return container;
    }

    public static Map<String, List<Lesson>> sortLessonsToMap(ScheduleSolution scheduleSolution) {
        Map<String, List<Lesson>> schedule = new HashMap<>();
        for (Semester semester : scheduleSolution.getSemesters()) {
            String semesterName = semester.getShortName();
            if (!schedule.containsKey(semesterName)) {
                List<Lesson> lessons = new ArrayList<>();
                for (Lesson lesson : scheduleSolution.getLessons()) {
                    if (lesson.getCourse().getSemester().getShortName().equals(semesterName)) {
                        lessons.add(lesson);
                    }
                }
                schedule.put(semester.getShortName(), lessons);
            }
        }
        return schedule;
    }


}
