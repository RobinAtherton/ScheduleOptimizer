package data.input;

import app.ScheduleSolution;
import data.output.ConstraintLogger;

public class Initializer {

    /**
     *
     * @param scheduleSolution solution to fill.
     * @param initFlag 1 for Filtered Access. 0 for Total Access.
     * @throws Exception
     */
    public static void init(ScheduleSolution scheduleSolution, int initFlag) throws Exception {
        DataInput.init();
        if (initFlag == 0) {
            fillSolution(scheduleSolution);
        } else if (initFlag == 1) {
            fillSolutionFiltered(scheduleSolution);
        }
        ConstraintLogger.init(scheduleSolution);
    }

    private static void fillSolution(ScheduleSolution scheduleSolution) {
        scheduleSolution.setPeriods(DataInput.getPeriods());
        scheduleSolution.setRooms(DataInput.getRooms());
        scheduleSolution.setLecturers(DataInput.getLecturers());
        scheduleSolution.setPreferences(DataInput.getPreferences());
        scheduleSolution.setLessons(DataInput.getLessons());
        scheduleSolution.setSemesters(DataInput.getSemesters());
        scheduleSolution.setCourses(DataInput.getCourses());
        scheduleSolution.setSubjects(DataInput.getSubjects());
        scheduleSolution.setStudents(DataInput.getStudents());
    }

    private static void fillSolutionFiltered(ScheduleSolution scheduleSolution) {
        scheduleSolution.setPeriods(DataInput.getPeriods());
        scheduleSolution.setRooms(DataInput.getRooms());
        scheduleSolution.setLecturers(DataInput.getLecturers());
        scheduleSolution.setPreferences(DataInput.getPreferences());
        scheduleSolution.setLessons(DataInput.getFilteredLessons());
        scheduleSolution.setSemesters(DataInput.getFilteredSemesters());
        scheduleSolution.setCourses(DataInput.getFilteredCourses());
        scheduleSolution.setSubjects(DataInput.getSubjects());
        scheduleSolution.setStudents(DataInput.getStudents());
    }
}
