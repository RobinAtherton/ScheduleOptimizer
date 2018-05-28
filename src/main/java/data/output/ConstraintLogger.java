package data.output;

import app.ScheduleSolution;
import domain.Lesson;
import domain.Preference;
import domain.Room;
import domain.Student;
import logic.CollisionDetector;

import java.util.ArrayList;
import java.util.List;

public class ConstraintLogger {

    private static ScheduleSolution solution;

    public static void init(ScheduleSolution scheduleSolution) {
        solution = scheduleSolution;
    }

    public static void logSolution() {
        System.out.println("################################################################");
        System.out.println("-----------------------Miscellaneous----------------------------");
        System.out.println("################################################################");
        printFWPMLessons();
        //printCoupledLessons();
        System.out.println("################################################################");
        System.out.println("-----------------------Hard Constraints-------------------------");
        System.out.println("################################################################");
        printLecturerCollisions();
        printRoomCollisions();
        printColliding();
        System.out.println("################################################################");
        System.out.println("-----------------------Soft Constraints-------------------------");
        System.out.println("################################################################");
        printPreferenceCollisions();
        printOnlyLessons();
        //printSoftColliding();

    }

    private static void printColliding() {
        System.out.println("Collisions: ");
        int collisions = 0;
        List<Lesson> passed = new ArrayList<>();
        for (Lesson outer : solution.getLessons()) {
            for (Lesson inner : solution.getLessons()) {
                if (CollisionDetector.extensiveCollision(outer, inner)) {
                    if (outer.getCourse().getSemester().getShortName().equals(inner.getCourse().getSemester().getShortName())) {
                        if (!passed.contains(outer) || !passed.contains(inner)) {
                            System.out.println("\t" + "Colliding: " + outer.getId() + " " + inner.getId());
                            System.out.println("\t" + outer.toString() + "\t" + inner.toString());
                            System.out.println("\t" + "GKW: " + outer.getGKWFlag() + " " + inner.getGKWFlag());
                            System.out.println("\t" + "UKW: " + outer.getUKWFlag() + " " + inner.getUKWFlag());
                            System.out.println("\t" + "ALT: " + outer.getAltId() + " " + inner.getAltId());
                            System.out.println("\t" + "FWPM: " + outer.isFWPM() + " " + inner.isFWPM());
                            System.out.println("\t" + "COLLISIONREASON: " + inner.getCollisionReason());
                            List<Student> collidingStudents = new ArrayList<>();
                            for (Student outerStud : outer.getFWPMStudents()) {
                                for (Student innerStud : inner.getFWPMStudents()) {
                                    if (outerStud.equals(innerStud) && !collidingStudents.contains(outerStud)) {
                                        collidingStudents.add(outerStud);
                                    }
                                }
                            }
                            System.out.println("\t" + "CollidingStudents: " + collidingStudents);
                            System.out.println("\n");
                            passed.add(outer);
                            passed.add(inner);
                            collisions++;
                        }
                    }
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No Collisions detected.");
        } else {
            System.out.println("Collisions detected: " + collisions);
        }
        System.out.println("\n");
    }

    private static void printOnlyLessons() {
        System.out.println("Only lessons on a single Day: ");
        int collisions = 0;
        List<Integer> passed = new ArrayList<>();
        for (Lesson lesson : solution.getLessons()) {
            if (lesson.getSameDay().size() == 0) {
                if (!passed.contains(lesson.getId())) {
                    System.out.println(lesson.toString() + " at: " + lesson.getPeriod().toString());
                    passed.add(lesson.getId());
                    collisions++;
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No single Lesson days.");
        } else {
            System.out.println("Single Lesson days: " + collisions);
        }
        System.out.println("\n");
    }

    private static void printLecturerCollisions() {
        System.out.println("LecturerCollisions: ");
        int collisions = 0;
        List<String> passed = new ArrayList<>();
        for (Lesson outer : solution.getLessons()) {
            for (Lesson inner : solution.getLessons()) {
                if (CollisionDetector.coupledCollision(outer, inner)) {
                    if (outer.getCourse().getLecturer().getShortName().equals(inner.getCourse().getLecturer().getShortName())
                            && outer.getPeriod().getDay() == inner.getPeriod().getDay()
                            && outer.getPeriod().getHour() == inner.getPeriod().getHour()) {
                        if (!passed.contains(outer.getId() + "" + inner.getId())) {
                            System.out.println("\t" + "Lecturer Colliding: " + outer.getId() + " " + inner.getId());
                            System.out.println("\t" + outer.toString() + " " + inner.toString());
                            System.out.println("\t" + "GKW: " + outer.getGKWFlag() + " " + inner.getGKWFlag());
                            System.out.println("\t" + "UKW: " + outer.getUKWFlag() + " " + inner.getUKWFlag());
                            System.out.println("\t" + outer.getCourse().getLecturer().getShortName() + " " + inner.getCourse().getLecturer().getShortName());
                            passed.add(outer.getId() + "" + inner.getId());
                            collisions++;
                        }
                    }
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No Lecturer Collisions detected.");
        } else {
            System.out.println("Lecturer Collisions detected: " + collisions);
        }
        System.out.println("\n");
    }

    private static void printRoomCollisions() {
        System.out.println("RoomCollisions: ");
        int collisions = 0;
        List<Room> passed = new ArrayList<>();
        for (Lesson outer : solution.getLessons()) {
            for (Lesson inner : solution.getLessons()) {
                if (CollisionDetector.coupledCollision(outer, inner)) {
                    if (outer.getRoom().getNumber().equals(inner.getRoom().getNumber())
                            && outer.getPeriod().getDay() == inner.getPeriod().getDay()
                            && outer.getPeriod().getHour() == inner.getPeriod().getHour()) {
                        if (!passed.contains(outer.getRoom()) && !passed.contains(inner.getRoom())) {
                            System.out.println("\t" + "Room Colliding: " + outer.getId() + " " + inner.getId());
                            System.out.println("\t" + outer.toString() + " " + inner.toString());
                            System.out.println("\t" + "GKW: " + outer.getGKWFlag() + " " + inner.getGKWFlag());
                            System.out.println("\t" + "UKW: " + outer.getUKWFlag() + " " + inner.getUKWFlag());
                            System.out.println("\t" + outer.getRoom().getNumber() + " " + inner.getRoom().getNumber());
                            System.out.println("\n");
                            passed.add(outer.getRoom());
                            passed.add(inner.getRoom());
                            collisions++;
                        }
                    }
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No Room Collisions detected.");
        } else {
            System.out.println("Room Collisions detected: " + collisions);
        }
        System.out.println("\n");
    }

    private static void printSoftColliding() {
        System.out.println("Soft Collisions: ");
        int softCollisions = 0;
        List<String> passed = new ArrayList<>();
        for (Lesson outer : solution.getLessons()) {
            for (Lesson inner : solution.getLessons()) {
                if (CollisionDetector.softFWPMCollision(outer, inner)) {
                    if (!passed.contains(outer.getId() + "" + inner.getId())) {
                        System.out.println("\t" + "SoftColliding: " + outer.getId() + " " + inner.getId());
                        System.out.println("\t" + outer.toString() + "\t" + inner.toString());
                        System.out.println("\t" + "FWPM: " + outer.isFWPM() + " " + inner.isFWPM());
                        System.out.println("\t" + "LEFT: " + outer.getFWPMStudents());
                        System.out.println("\t" + "RIGHT: " + inner.getFWPMStudents());
                        List<Student> commonStudents = new ArrayList<>();
                        for (Student outerS : outer.getFWPMStudents()) {
                            for (Student innerS : inner.getFWPMStudents()) {
                                if (outerS.equals(innerS)) {
                                    commonStudents.add(innerS);
                                }
                            }
                        }
                        System.out.println("\t" + "IN_COMMON: " + commonStudents);
                        System.out.println("\n");
                        passed.add(inner.getId() + "" + outer.getId());
                        softCollisions++;
                    }
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No Soft Collisions detected.");
        } else {
            System.out.println("Soft Collisions detected:" + softCollisions);
        }
        System.out.println("\n");
    }

    private static void printPreferenceCollisions() {
        System.out.println("Preference Collisions:");
        List<String> passed = new ArrayList<>();
        int preferences = 0;
        for (Lesson outer : solution.getLessons()) {
            for (Preference inner : solution.getPreferences()) {
                if (CollisionDetector.preferenceCollision(outer, inner)) {
                    if (!(passed.contains(outer.getId() + "-" + inner.getDay() + "-" + inner.getHour()))) {
                        String tempC = "";
                        if (inner.getConstraint() > 0) {
                            tempC = "Positiv.";
                        } else {
                            tempC = "Negativ.";
                        }
                        if (inner.getConstraint() < 0) {
                            System.out.println("\t" + "PreferenceCollision: " + outer.getId() + " " + outer.getCourse().getSubject().getShortName() +
                                    "\t" + "\t" + inner.getLecturer().getShortName() + " at Day: " + inner.getDay() + " and Hour: " + inner.getHour() + " " + outer.getCourse().getSemester().getShortName() + " " + tempC);
                            preferences++;
                        }
                        passed.add(outer.getId() + "-" + inner.getDay() + "-" + inner.getHour());
                    }
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No Preference Collisions detected.");
        } else {
            System.out.println("Preference Collisions detected: " + preferences);
        }
        System.out.println("\n");
    }

    private static void printFWPMLessons() {
        System.out.println("FWPM Lessons: ");
        List<Lesson> passed = new ArrayList<>();
        int amount = 0;
        for (Lesson lesson : solution.getLessons()) {
            if (lesson.isFWPM()) {
                if (!passed.contains(lesson)) {
                    System.out.println("\t" + lesson.toString() + " at " + lesson.getPeriod().toString());
                    amount++;
                    passed.add(lesson);
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No FWPM Lessons detected.");
        } else {
            System.out.println("FWPM Lessons detected: " + amount);
        }
        System.out.println("\n");
    }

    private static void printCoupledLessons() {
        System.out.println("Coupled Lessons: ");
        List<Lesson> passed = new ArrayList<>();
        int amount = 0;
        for (Lesson lesson : solution.getLessons()) {
            if (lesson.getCoupledLessons().size() > 0) {
                if (!passed.contains(lesson)) {
                    System.out.println("\t" + lesson.toString() + " at: " + lesson.getPeriod().toString());
                    passed.add(lesson);
                    for (Lesson inner :lesson.getCoupledLessons()) {
                        System.out.println("\t" + "\t" + inner.toString() + " at: " + lesson.getPeriod().toString());
                        passed.add(inner);
                    }
                    System.out.println("\n");
                    amount++;
                }
            }
        }
        if (passed.isEmpty()) {
            System.out.println("No Coupled Lessons detected.");
        } else {
            System.out.println("Coupled Lessons detected: " + amount);
        }
        System.out.println("\n");
    }

}
