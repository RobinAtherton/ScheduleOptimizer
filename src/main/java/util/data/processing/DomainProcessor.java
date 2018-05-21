package util.data.processing;

import domain.Lesson;
import domain.Student;
import util.fileHandling.FileLoader;
import util.helpers.own.logic.Formater;
import util.ruleLogic.CollisionDetector;

import java.io.*;
import java.util.*;

public class DomainProcessor {

    //Refactored

    public static void assertFilteredFlags(List<Lesson> lessons, List<Student> students) throws IOException {
        assertPrimaryFlags(lessons);
        removeNonPrimaryLessons(lessons);
        removeNoSemesterLessons(lessons);
        assertUKWFlags(lessons, FileLoader.getFilteredUKWLessonsFile());
        assertGKWFlags(lessons, FileLoader.getFilteredGKWLessonsFile());
        assertGroupFlags(lessons, FileLoader.getFilteredGroupedLessonsFile());
        assertAltId(lessons, FileLoader.getFilteredAltLessonsFile());
        assertFWPMFlags(lessons, students, FileLoader.getFilteredFWPMLessonsFile());
        assertSameDay(lessons);
        assertCoupled(lessons);
        assertPracticalFlag(lessons);
        assertDodgeable(lessons);
        assertSameSemester(lessons);
        assertBlockedLessons(lessons);
    }

    public static void assertFlags(List<Lesson> lessons, List<Student> students) throws IOException {
        assertPrimaryFlags(lessons);
        removeNonPrimaryLessons(lessons);
        removeNoSemesterLessons(lessons);
        assertUKWFlags(lessons, FileLoader.getModifiedUKWLessonsFile());
        assertGKWFlags(lessons, FileLoader.getModifiedGKWLessonsFile());
        assertGroupFlags(lessons, FileLoader.getModifiedGroupedLessonsFile());
        assertAltId(lessons, FileLoader.getModifiedAltLessonsFile());
        assertFWPMFlags(lessons, students, FileLoader.getModifiedFWPMLessonsFile());
        assertSameDay(lessons);
        assertCoupled(lessons);
        assertPracticalFlag(lessons);
        assertDodgeable(lessons);
        assertSameSemester(lessons);
        assertBlockedLessons(lessons);

    }

    private static void assertPrimaryFlags(List<Lesson> lessons) {
        Iterator<Lesson> outerIterator = lessons.iterator();
        while (outerIterator.hasNext()) {
            Iterator<Lesson> innerIterator = lessons.iterator();
            Lesson outer = outerIterator.next();
            Set<Lesson> coupledLessons = new HashSet<>();
            while (innerIterator.hasNext()) {
                Lesson inner = innerIterator.next();
                if (inner.getId() == outer.getId()
                        && inner.getLecturerName().equals(outer.getLecturerName())
                        && inner.getSubjectName().equals(outer.getSubjectName())
                        && inner.getSemesterName().equals(outer.getSemesterName())
                        && inner.getRoomName().equals(outer.getRoomName())
                        && inner.getDay() == outer.getDay()
                        && inner.getHour() != outer.getHour()) {
                    coupledLessons.add(inner);
                }
            }
            int MIN = Integer.MAX_VALUE;
            for (Lesson lesson : coupledLessons) {
                if (lesson.getHour() < MIN) {
                    MIN = lesson.getHour();
                }
            }
            if (outer.getHour() < MIN && MIN != Integer.MAX_VALUE) {
                outer.setPrime(true);
                outer.setBlockLength(1 + coupledLessons.size());
            }
        }
    }

    private static void removeNonPrimaryLessons(List<Lesson> lessons) {
        Iterator<Lesson> iterator = lessons.iterator();
        while (iterator.hasNext()) {
            Lesson next = iterator.next();
            if (!next.isPrime()) {
                iterator.remove();
            }
        }
    }

    private static void assertUKWFlags(List<Lesson> lessons, File file) throws IOException {
        List<String[]> comparison = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        fillComparison(comparison, br, line);
        Iterator<Lesson> iterator = lessons.iterator();
        while (iterator.hasNext()) {
            Lesson next = iterator.next();
            for (String[] parts : comparison) {
                if (next.getId() == Integer.parseInt(parts[0])
                        && next.getSemesterName().equals(parts[4])
                        && next.getLecturerName().equals(parts[5])
                        && next.getSubjectName().equals(parts[6])) {
                    next.setUKWFlag(true);
                }
            }
        }
    }

    private static void assertGKWFlags(List<Lesson> lessons, File file) throws IOException {
        List<String[]> comparison = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        fillComparison(comparison, br, line);
        Iterator<Lesson> iterator = lessons.iterator();
        while (iterator.hasNext()) {
            Lesson next = iterator.next();
            for (String[] parts : comparison) {
                if (next.getId() == Integer.parseInt(parts[0])
                        && next.getSemesterName().equals(parts[4])
                        && next.getLecturerName().equals(parts[5])
                        && next.getSubjectName().equals(parts[6])) {
                    next.setGKWFlag(true);
                }
            }
        }
    }

    private static void assertAltId(List<Lesson> lessons, File file) throws IOException {
        List<String[]> comparison = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        fillComparison(comparison, br, line);
        Iterator<Lesson> iterator = lessons.iterator();
        while (iterator.hasNext()) {
            Lesson next = iterator.next();
            for (String[] parts : comparison) {
                if (next.getId() == Integer.parseInt(parts[0])
                        && next.getSemesterName().equals(parts[4])
                        && next.getLecturerName().equals(parts[5])
                        && next.getSubjectName().equals(parts[6])) {
                    if (parts[11].equals("") || parts[11].isEmpty()) {
                        next.setAltId(0);
                    } else {
                        next.setAltId(Integer.parseInt(parts[11]));
                    }
                }
            }
        }
    }

    private static void fillComparison(List<String[]> comparison, BufferedReader br, String line) throws IOException {
        while (line != null) {
            String[] parts = Formater.clean(line);
            comparison.add(parts);
            line = br.readLine();
        }
    }

    private static void assertGroupFlags(List<Lesson> lessons, File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int fileRows = Formater.count(file.getAbsolutePath());
        String line = br.readLine();
        for (int i = 0; i < fileRows; i++) {
            String[] parts = Formater.clean(line);
            Iterator<Lesson> iterator = lessons.iterator();
            while (iterator.hasNext()) {
                Lesson next = iterator.next();
                if (next.getId() == Integer.parseInt(parts[0])
                        && next.getSemesterName().equals(parts[4])
                        && next.getLecturerName().equals(parts[5])
                        && next.getSubjectName().equals(parts[6])) {
                    for (String part : parts) {
                        if (part.contains("Gruppe")) {
                            assertGroup(next, part);
                        }
                    }
                }
            }
            line = br.readLine();
        }
    }

    private static void assertGroup(Lesson lesson, String part) {
        String[] split = part.split(" ");
        String[] splitSplit = split[1].split("/");
        lesson.getGroupList().addAll(Arrays.asList(splitSplit));
    }

    private static void assertFWPMFlags(List<Lesson> lessons, List<Student> students, File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int fileRows = Formater.count(file.getAbsolutePath());
        String line = br.readLine();
        for (int i = 0; i < fileRows; i++) {
            String[] parts = Formater.clean(line);
            Iterator<Lesson> iterator = lessons.iterator();
            while (iterator.hasNext()) {
                Lesson n = iterator.next();
                if (n.getId() == Integer.parseInt(parts[0])
                        && n.getSemesterName().equals(parts[4])
                        && n.getLecturerName().equals(parts[5])
                        && n.getSubjectName().equals(parts[6])) {
                    for (String p : parts) {
                        if (p.contains("FWPM") || p.contains("FWPF")) {
                            n.setFWPM(true);
                            assignStudentsToLesson(students, n);
                        }

                    }

                }
            }
            line = br.readLine();
        }
    }

    private static void assignStudentsToLesson(List<Student> students, Lesson n) {
        for (Student s : students) {
            if (s.getAttendedFWPM().contains(n.getSubjectName())) {
                if (n.getSemesterName().contains(s.getSemesterName())) {
                    n.addFWPMStudent(s);
                }
            }
        }
    }

    private static void removeNoSemesterLessons(List<Lesson> lessons) {
        Iterator<Lesson> iterator = lessons.iterator();
        while (iterator.hasNext()) {
            Lesson next = iterator.next();
            if (next.getSemesterName() == "NO_SEMESTER") {
                iterator.remove();
            }
        }
    }

    private static void assertSameDay(List<Lesson> lessons) {
        for (Lesson left : lessons) {
            for (Lesson right : lessons) {
                if (left.getPeriod().getDay() == right.getPeriod().getDay()
                        && left.getSemesterName().equals(right.getSemesterName())) {
                    if (!left.equals(right)) {
                        left.addSameDay(right);
                    }
                }
            }

        }

    }

    private static void assertCoupled(List<Lesson> lessons) {
        for (Lesson outer : lessons) {
            for (Lesson inner : lessons) {
                if (!inner.equals(outer)
                        && inner.getId() == outer.getId()
                        && inner.getDay() == outer.getDay()
                        && inner.getHour() == outer.getHour()) {
                    outer.getCoupledLessons().add(inner);
                }
            }
        }
    }

    private static void assertPracticalFlag(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            practicalHelper(lesson.getSubjectName(), lesson);
        }
    }

    private static void practicalHelper(String s, Lesson lesson) {
        if (s.startsWith("p")) {
            if (!s.equals("prg")
                    && !s.equals("prg2") && !s.equals("prg2a")
                    && !s.equals("prg3")
                    && !s.equals("praes")
                    && !s.equals("pm")
                    && !s.equals("proz") && !s.equals("proza")
                    ) {
                lesson.setPractical(true);
            }
        }
    }

    private static void assertDodgeable(List<Lesson> lessons) {
        for (Lesson outer : lessons) {
            for (Lesson inner : lessons) {
                if (outer.getId() != inner.getId()
                        && outer.getSemesterName().equals(inner.getSemesterName())
                        && outer.getSubjectName().equals(inner.getSubjectName())
                        && outer.isPractical() && inner.isPractical()) { //TODO Check if necessary
                    outer.getDodgeableLessons().add(inner);
                }
            }
        }
    }

    private static void assertSameSemester(List<Lesson> lessons) {
        for (Lesson outer : lessons) {
            for (Lesson inner : lessons) {
                if (outer.getId() != inner.getId() //TODO Review if there are Same ID's within same Semester
                        && outer.getSemesterName().equals(inner.getSemesterName())) {
                    outer.getSameSemester().add(inner);
                }
            }
        }
    }

    private static void assertBlockedLessons(List<Lesson> lessons) {
        for (Lesson outer : lessons) {
            for (Lesson inner : lessons) {
                if (outer.getId() != inner.getId()) {
                    fwpmBlockingHelper(outer, inner);
                    practicalBlockingHelper(outer, inner);
                }
            }
        }
    }

    private static void fwpmBlockingHelper(Lesson left, Lesson right) {
        if (left.getSemesterName().equals(right.getSemesterName())
                && left.isFWPM() ^ right.isFWPM()) {
            if (CollisionDetector.basicCollision(left, right)) {
                if (left.isFWPM()) {
                    if (!right.getFwpmBlocked().contains(left)) {
                        right.getFwpmBlocked().add(left);
                    }
                }
                if (right.isFWPM()) {
                    if (!left.getFwpmBlocked().contains(right)) {
                        left.getFwpmBlocked().add(right);
                    }
                }
            }
        }
    }

    private static void practicalBlockingHelper(Lesson left, Lesson right) {
        if (left.getSemesterName().equals(right.getSemesterName())) {
            if (CollisionDetector.basicCollision(left, right)) {
                if (left.isPractical()) {
                    if (!right.getPracticalBlocked().contains(left)) {
                        right.getPracticalBlocked().add(left);
                    }
                }
                if (right.isPractical()) {
                    if (!left.getPracticalBlocked().contains(left)) {
                        left.getPracticalBlocked().add(right);
                    }
                }
            }
        }
    }
}

