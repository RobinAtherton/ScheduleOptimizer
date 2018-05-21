package util.data.processing;

import domain.*;
import util.helpers.own.logic.Formater;
import util.helpers.own.container.PreferenceType;

import java.io.*;
import java.util.*;

public class DomainGenerator {

    public static List<Lecturer> readLecturerData(File input) throws IOException {
        List<Lecturer> lecturers = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Lecturer lecturer = new Lecturer();
            if (!parts[0].equals(null) && !parts[0].equals("")) {
                lecturer.setShortName(parts[0]);
            } else {
                lecturer.setShortName("NO_NAME");
            }
            lecturer.setLongName(parts[1]);
            lecturer.setTitle(parts[23]);
            lecturers.add(lecturer);
            line = br.readLine();
        }
        return lecturers;
    }

    public static List<Subject> readSubjectData(File input) throws IOException {
        List<Subject> subjects = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Subject subject = new Subject();
            subject.setShortName(parts[0]);
            subject.setLongName(parts[1]);
            subject.setBaseRoom(parts[3]);
            subjects.add(subject);
            line = br.readLine();
        }
        return subjects;
    }

    public static List<Room> readRoomData(File input) throws IOException {
        List<Room> rooms = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Room room = new Room();

            room.setNumber(parts[0]);
            room.setType(parts[1]);
            if (parts[7].equals("") || parts[7].equals(" ") || parts[7].isEmpty()) {
                room.setCapacity(0);

            } else {
                room.setCapacity(Integer.parseInt(parts[7]));

            }

            rooms.add(room);
            line = br.readLine();
        }
        return rooms;
    }

    public static List<Semester> readSemesterData(File input) throws IOException {
        List<Semester> semesters = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Semester semester = new Semester();
            semester.setShortName(parts[0]);
            semester.setLongName(parts[1]);
            semester.setCapacity(Integer.parseInt(parts[17]));
            if (!line.contains("IN") || !line.contains("N"))
                semesters.add(semester);
            line = br.readLine();
        }
        return semesters;
    }

    public static List<Preference> readPreferenceData(File input, List<Lecturer> lecturers) throws IOException {
        List<Preference> preferences = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Preference preference = new Preference();
            findTypeOfPreference(parts, preference);
            preference.setDay(Integer.parseInt(parts[2]));
            preference.setHour(Integer.parseInt(parts[3]));
            preference.setConstraint(Integer.parseInt(parts[4]));
            for (Lecturer lecturer : lecturers) {
                if (lecturer.getShortName().equals(parts[1])) {
                    preference.setLecturer(lecturer);
                }
            }
            if (preference.getLecturer() == null) {
                preference.setLecturer(new Lecturer(1));
            }
            preferences.add(preference);
            line = br.readLine();
        }
        return preferences;
    }

    private static void findTypeOfPreference(String[] parts, Preference preference) {
        if (parts[0].equals("L")) {
            preference.setType(PreferenceType.L);
        } else if (parts[0].equals("R")) {
            preference.setType(PreferenceType.R);
        } else if (parts[0].equals("K")) {
            preference.setType(PreferenceType.K);
        }
    }

    public static List<Period> readPeriodData(File input) throws IOException {
        Set<Period> periods = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Period period = new Period();
            period.setDay(Integer.parseInt(parts[5]));
            period.setHour(Integer.parseInt(parts[6]));
            period.setId(Integer.parseInt(parts[0]));
            periods.add(period);
            line = br.readLine();
        }
        return new ArrayList<>(periods);
    }

    public static List<Period> generatePeriodData() {
        List<Period> periods = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 12; j++) {
                Period period = new Period();
                period.setHour(j);
                period.setDay(i);
                periods.add(period);
            }
        }
        return periods;
    }

    public static List<Course> readCourseData(File input, List<Semester> semesters, List<Lecturer> lecturers, List<Subject> subjects) throws IOException {
        List<Course> courses = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Course course = new Course();
            course.setId(Integer.parseInt(parts[0]));
            for (Semester semester : semesters) {
                if (semester.getShortName().equals(parts[1])) {
                    course.setSemester(semester);
                    break;
                }
            }
            for (Lecturer lecturer : lecturers) {
                if (lecturer.getShortName().equals(parts[2])) {
                    course.setLecturer(lecturer);
                    break;
                }
            }
            for (Subject subject : subjects) {
                if (subject.getShortName().equals(parts[3])) {
                    course.setSubject(subject);
                    break;
                }
            }
            if (course.getSemester() == null) {
                course.setSemester(new Semester(1));
            }
            if (course.getSubject() == null) {
                course.setSubject(new Subject(1));
            }
            if (course.getLecturer() == null) {
                course.setLecturer(new Lecturer(1));
            }
            courses.add(course);
            line = br.readLine();
        }
        return courses;
    }

    private static void roomFind(List<Room> rooms, String[] parts, Lesson lesson) {
        for (Room room : rooms) {
            if (parts[4].contains("~")) {
                String[] split = parts[4].split("~");
                parts[4] = split[1];
            }
            if (parts[4].isEmpty()) {
                lesson.setRoom(new Room(1));
            }
            if (room.getNumber().equals(parts[4])) {
                lesson.setRoom(room);
                break;//TODO
            }
        }
    }

    public static List<Lesson> readLessonData(File input, List<Room> rooms, List<Lecturer> lecturers, List<Subject> subjects, List<Semester> semesters, List<Period> periods) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(input));
        List<Lesson> lessons = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            String[] parts = Formater.clean(line);
            Lesson lesson = new Lesson();
            Course course = new Course();
            lesson.setId(Integer.parseInt(parts[0]));
            for (Semester semester : semesters) {
                if (semester.getShortName().equals(parts[1])) {
                    course.setSemester(semester);
                    break;
                }
            }
            for (Lecturer lecturer : lecturers) {
                if (lecturer.getShortName().equals(parts[2])) {
                    course.setLecturer(lecturer);
                    break;
                }
            }
            for (Subject subject : subjects) {
                if (subject.getShortName().equals(parts[3])) {
                    course.setSubject(subject);
                    break;
                }
            }
            for (Period period : periods) {
                if (period.getHour() == Integer.parseInt(parts[6]) &&
                        period.getDay() == Integer.parseInt(parts[5])) {
                    lesson.setPeriod(period);
                }
            }
            roomFind(rooms, parts, lesson);
            if (course.getSemester() == null) {
                course.setSemester(new Semester(1));
            }
            if (course.getSubject() == null) {
                course.setSubject(new Subject(1));
            }
            if (course.getLecturer() == null) {
                course.setLecturer(new Lecturer(1));
            }
            lesson.setCourse(course);
            lessons.add(lesson);
            line = br.readLine();
        }
        return lessons;
    }

    public static List<Student> readStudentData(File input, List<Subject> subjects) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(input));
        Map<Integer, List<String>> studentMap = new HashMap<>();
        List<Student> students = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        for (String outer : lines) {
            Student student = null;
            int addFlag = 0;
            String[] parts = outer.split(",");
            if (students != null && !students.isEmpty()) {
                for (Student s : students) {
                    if (s.getMtrNumber() == Integer.parseInt(parts[5])) {
                        student = s;
                        addFlag = 1;
                        s.addFWPM(parts[0]);
                        break;
                    }
                }
            }
            if (student == null) {
                student = new Student();
                student.setMtrNumber(Integer.parseInt(parts[5]));
                student.setSemester(parts[2]);
                student.addFWPM(parts[0]);
            }
            if (addFlag != 1) {
                students.add(student);
            }
            addFlag = 0;
        }
        // 0 = subjectShortName, 1 = FWPM, 2 = typeOfSemester, 3 = SubjectFullName, 5 = mtrNumber;
        return students;
    }
}

