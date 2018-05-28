package data.input;

import domain.*;
import data.processing.DomainGenerator;
import data.filehandling.FileLoader;
import data.processing.DomainProcessor;

import java.io.IOException;
import java.util.List;

public class DataInput {

    //lowLevel
    private static List<Room> rooms;
    private static List<Lecturer> lecturers;
    private static List<Subject> subjects;
    private static List<Semester> semesters;
    private static List<Student> students;

    //combination
    private static List<Preference> preferences;
    private static List<Period> periods;
    private static List<Course> courses;
    private static List<Lesson> lessons;

    //filtered
    private static List<Semester> filteredSemesters;
    private static List<Lesson> filteredLessons;
    private static List<Course> filteredCourses;

    public static void init() throws Exception {
        FileLoader.load();
        initLowLevelDomains();
        initCombinationDomains();
        initFiltered();
        initModified();
        System.out.println("\n");
    }

    private static void initLowLevelDomains() throws IOException {
        rooms = DomainGenerator.readRoomData(FileLoader.getRoomsFile());
        lecturers = DomainGenerator.readLecturerData(FileLoader.getLecturersFile());
        subjects = DomainGenerator.readSubjectData(FileLoader.getSubjectsFile());
        semesters = DomainGenerator.readSemesterData(FileLoader.getSemestersFile());
        System.out.println("Low Level Domains Initialized.");
    }

    private static void initCombinationDomains() throws Exception {
        preferences = DomainGenerator.readPreferenceData(FileLoader.getPreferencesFile(), lecturers);
        periods = DomainGenerator.generatePeriodData();
        courses = DomainGenerator.readCourseData(FileLoader.getLessonsFile(), semesters, lecturers, subjects);
        lessons = DomainGenerator.readLessonData(FileLoader.getLessonsFile(), rooms, lecturers, subjects, semesters, periods);
        students = DomainGenerator.readStudentData(FileLoader.getFWPMAssertedOccupancyFile(), subjects);
        System.out.println("Combined Domains Initialized.");
    }

    private static void initFiltered() throws Exception {
        initFilteredSemesters();
        initFilteredCombinationDomains();
        initFilteredPrimeLessons();
        System.out.println("Filtered Domains Initialized.");
    }

    private static void initFilteredCombinationDomains() throws Exception {
        filteredCourses = DomainGenerator.readCourseData(FileLoader.getLessonsFile(), filteredSemesters, lecturers, subjects);
        filteredLessons = DomainGenerator.readLessonData(FileLoader.getLessonsFile(), rooms, lecturers, subjects, filteredSemesters, periods);
    }

    private static void initFilteredSemesters() throws IOException {
        filteredSemesters = DomainGenerator.readSemesterData(FileLoader.getFilteredSemestersFile());
    }

    private static void initFilteredPrimeLessons() throws IOException {
        DomainProcessor.assertFilteredFlags(filteredLessons, students);
        System.out.println("Filtered Lessons asserted.");
    }

    private static void initModified() throws IOException {
        initPrimeLessons();
        System.out.println("Unfiltered Domains Initialized.");
    }

    private static void initPrimeLessons() throws IOException {
        DomainProcessor.assertFlags(lessons, students);
        System.out.println("Unfiltered Lessons asserted");
    }

    public static List<Room> getRooms() {
        return rooms;
    }

    public static List<Lecturer> getLecturers() {
        return lecturers;
    }

    public static List<Subject> getSubjects() {
        return subjects;
    }

    public static List<Semester> getSemesters() {
        return semesters;
    }

    public static List<Preference> getPreferences() {
        return preferences;
    }

    public static List<Period> getPeriods() {
        return periods;
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static List<Lesson> getLessons() {
        return lessons;
    }

    public static List<Semester> getFilteredSemesters() {
        return filteredSemesters;
    }

    public static List<Lesson> getFilteredLessons() {
        return filteredLessons;
    }

    public static List<Course> getFilteredCourses() {
        return filteredCourses;
    }

    public static List<Student> getStudents() {
        return students;
    }
}
