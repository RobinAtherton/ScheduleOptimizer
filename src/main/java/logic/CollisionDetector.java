package logic;

import domain.Lesson;
import domain.Period;
import domain.Preference;
import domain.Student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollisionDetector {

    private static boolean COLLISION = true;
    private static boolean NO_COLLISION = false;

    //REFACTORED
    public static boolean isCollision2(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (!left.isFWPM() && !right.isFWPM()) {
                Boolean x = normalCollision(left, right);
                if (x != null) return x;
            } else if (left.isFWPM() ^ right.isFWPM()) {
                if (baseCollision(left, right)) {
                    if (!left.isFWPM() && right.isFWPM()) {
                        return mustToFwpmCollision(left, right);
                    } else {
                        return fwpmToMustCollision(left, right);
                    }
                }
            } else {
                if (baseCollision(left, right)) {
                    return isFWPMCollision(left, right);
                }
            }
        }
        return NO_COLLISION;
    }

    //REFACTORED REFACTORED
    public static boolean isCollision(Lesson left, Lesson right) {
        if (!left.isFWPM() && !right.isFWPM()) {
            Boolean x = normalCollision(left, right);
            if (x != null) return x;
        } else if (left.isFWPM() ^ right.isFWPM()) {
            Boolean x = fwpmNonFwpmCollision(left, right);
            if (x != null) return x;
        } else if (left.isFWPM() && right.isFWPM()){
            if (basicCollision(left, right)) {
                return isFWPMCollision(left, right);
            }
        }
        return NO_COLLISION;
    }

    private static Boolean fwpmNonFwpmCollision(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (baseCollision(left, right)) {
                if (left.getSemesterName().equals(right.getSemesterName())) {
                    if (!left.isFWPM() && right.isFWPM()) {
                        return mustToFwpmCollision(left, right);
                    } else {
                        return fwpmToMustCollision(left, right);
                    }
                }
            }
        }
        return null;
    }

    private static Boolean normalCollision(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (!checkForThreeWay(left)) {
                return NO_COLLISION;
            } else if (!checkAlternatives(left, right)) {
                return NO_COLLISION;
            } else {
                return baseCollision(left, right);
            }
        }
        return null;
    }

    private static boolean fwpmToMustCollision(Lesson left, Lesson right) {
        if (!right.isFWPM() && left.isFWPM()) {
            for (Lesson dodge : right.getDodgeableLessons()) {
                if (dodge.getFwpmBlocked().size() == 0) {
                    return NO_COLLISION;
                }
            }
            /*if (!isAdditionalPracticalFwpmException(left, right)) {
                return NO_COLLISION;
            }*/
        }
        left.setCollisionReason("FWPM - Pflicht Kollision");
        right.setCollisionReason("FWPM - Pflicht Kollision");
        return COLLISION;
    }

    private static boolean mustToFwpmCollision(Lesson left, Lesson right) {
        if (right.isFWPM() && !left.isFWPM()) {
            for (Lesson dodge : left.getDodgeableLessons()) {
                if (dodge.getFwpmBlocked().size() == 0) {
                    return NO_COLLISION;
                }
            }
            /*if (!isAdditionalPracticalFwpmException(left, right)) {
                return NO_COLLISION;
            }*/
        }
        left.setCollisionReason("FWPM - Pflicht Kollision");
        right.setCollisionReason("FWPM - Pflicht Kollision");
        return COLLISION;
    }

    private static boolean isFWPMCollision(Lesson left, Lesson right) {
        if (left.isFWPM() && right.isFWPM()) {
            for (Student leftS : left.getFWPMStudents()) {
                for (Student rightS : right.getFWPMStudents()) {
                    if (leftS.equals(rightS)) {
                        left.setCollisionReason("FWPM");
                        right.setCollisionReason("FWPM");
                        return COLLISION;
                    }
                }
            }
        }
        return NO_COLLISION;
    }

    private static boolean isAdditionalPracticalFwpmException(Lesson left, Lesson right) {
        int amountCollisions = 0;
        String temp = "";
        for (Lesson day : left.getSameDay()) {
            if (isTimeCollision(left, day)) {
                amountCollisions++;
            }
        }
        if (amountCollisions > 1) {
            left.setNeedsGroup("");
            right.setNeedsGroup("");
            return COLLISION;
        }
        if (!left.isFWPM() && right.isFWPM()) {
            temp = checkGroupDifferences1(left, right, temp);
            if (!temp.equals("")) {
                return NO_COLLISION;
            }
        }
        if (!right.isFWPM() && left.isFWPM()) {
            temp = checkGroupDifferences1(right, left, temp);
            if (!temp.equals("")) {
                return NO_COLLISION;
            }
        }
        left.setNeedsGroup("");
        right.setNeedsGroup("");
        return COLLISION;
    }

    private static String checkGroupDifferences1(Lesson left, Lesson right, String temp) {
        if (left.getGroupList().size() > 1) {
            if (right.getUKWFlag() || right.getAltId() == 1) {
                for (String group : left.getGroupList()) {
                    if (group.contains("2")) {
                        temp = group;
                    }
                }
                left.setNeedsGroup("Students visiting " + right.toString() + " need to choose Group " + temp + " for " + left.toString());
            }
            if (right.getGKWFlag() || right.getAltId() == 2) {
                for (String group : left.getGroupList()) {
                    if (group.contains("1")) {
                        temp = group;
                    }
                }
                left.setNeedsGroup("Students visiting " + right.toString() + " need to choose Group " + temp + " for " + left.toString());
            }
        }
        return temp;
    }

    private static String checkGroupDifferences(Lesson left, Lesson right) {
        List<String> temps = new ArrayList<>();
        String temp = "";
        int i = 0;
        if (left.getGroupList().size() > 1) {
            if (right.getUKWFlag() || right.getAltId() == 1) {
                for (String group : left.getGroupList()) {
                    if (group.contains("2")) {
                        temps.add(group);
                    }
                    if (group.contains("4")) {
                        temps.add(group);
                    }
                }
                for (String s : temps) {
                        i++;
                        temp += s;
                        if (temps.size() != 1 && i < temps.size()) {
                            temp += " or ";
                        }
                }
                left.setNeedsGroup("Students visiting " + right.toString() + " need to choose Group " + temp + " for " + left.toString());
            }
            if (right.getGKWFlag() || right.getAltId() == 2) {
                for (String group : left.getGroupList()) {
                    if (group.contains("1")) {
                        temps.add(group);
                    }
                    if (group.contains("3")) {
                        temps.add(group);
                    }
                }
                for (String s : temps) {
                    i++;
                    temp += s;
                    if (temps.size() != 1 && i < temps.size()) {
                        temp += " or ";
                    }
                }
                left.setNeedsGroup("Students visiting " + right.toString() + " need to choose Group " + temp + " for " + left.toString());
            }
        }
        return temp;
    }

    //----------------------------------------------------------------------------------------------------------------------------

    public static boolean notMoreThanThreeAtATime(Lesson lesson) {
        int counter = 0;
        for (Lesson same : lesson.getSameDay()) {
            if (isTimeCollision(lesson, same)) {
                counter++;
            }
        }
        if (counter > 3) {
            return COLLISION;
        }
        return NO_COLLISION;
    }

    private static boolean baseCollision(Lesson left, Lesson right) {
        if (isGroupCollision(left, right)) {
            if (isBiWeeklyCollision(left, right)) {
                return COLLISION;
            }
        }
        return NO_COLLISION;
    }

    private static boolean isBiWeeklyCollision(Lesson left, Lesson right) {
        if (isUKWCollision(left, right)) {
            left.setCollisionReason("UKW");
            right.setCollisionReason("UKW");
            return true;
        } else if (isGKWCollision(left, right)) {
            left.setCollisionReason("GKW");
            right.setCollisionReason("GKW");
            return true;
        } else if (isAltCollision(left, right)) {
            left.setCollisionReason("ALT");
            right.setCollisionReason("ALT");
            return true;
        }
        return false;
    }

    public static boolean basicCollision(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (baseCollision(left, right)) {
                return COLLISION;
            }
        }
        return NO_COLLISION;
    }

    public static boolean coupledCollision(Lesson left, Lesson right) {
        if (left.getCoupledLessons().contains(right) || right.getCoupledLessons().contains(left)) {
            return NO_COLLISION;
        } else {
            return basicCollision(left, right);
        }
    }

    //Experimental
    private static boolean checkForThreeWay(Lesson lesson) {
        List<Lesson> lessons = new ArrayList<>();
        for (Lesson outer : lesson.getSameDay()) {
            if (lesson.getSemesterName().equals(outer.getSemesterName())) {
                if (isTimeCollision(lesson, outer)) {
                    lessons.add(outer);
                }
            }
        }
        lessons.add(lesson);
        List<String> comboGroup = new ArrayList<>();
        int flag = 0;
        if (lessons.size() == 3) {
            for (Lesson outer : lessons) {
                if (outer.getGroupList().size() > 1) {
                    if (flag == 0) {
                        comboGroup = outer.getGroupList();
                        flag = 1;
                    } else if (flag > 0) {
                        return COLLISION;
                    }
                }
            }
        }
        Iterator<Lesson> lessonIterator = lessons.iterator();
        while (lessonIterator.hasNext()) {
            Lesson l = lessonIterator.next();
            if (l.getGroupList().size() > 1) {
                lessonIterator.remove();
            }
        }
        String sub = "";
        for (Lesson outer : lessons) {
            if (sub.equals("" )) {
                sub = outer.getSubjectName();
            } else {
                if (sub != outer.getSubjectName()) {
                    return COLLISION;
                }
            }
        }

        String temp1 = "";
        String temp2 = "";
        for (Lesson outer : lessons) {
            if (!outer.getGroupList().isEmpty()) {
                if (comboGroup.contains(outer.getGroupList().get(0)) && temp1.equals("")) {
                    temp1 = outer.getGroupList().get(0);
                } else if (comboGroup.contains(outer.getGroupList().get(0)) && !temp1.equals("")) {
                    temp2 = outer.getGroupList().get(0);
                }
            }
        }
        if (!temp1.equals("") && !temp2.equals("")) {
            if (temp1.equals(comboGroup.get(0)) && temp2.equals(comboGroup.get(1)) ||
                    temp1.equals(comboGroup.get(1)) && temp2.equals(comboGroup.get(0))) {
                return NO_COLLISION;
            }
        } else {
            return COLLISION;
        }
        return COLLISION;
    }

    private static boolean checkAlternatives(Lesson left, Lesson right) {
        if (!left.isPractical() || !right.isPractical()) {
            return COLLISION;
        }
        for (Lesson dodge : left.getDodgeableLessons()) {
            if (dodge.getFwpmBlocked().size() == 0 && dodge.getPracticalBlocked().size() == 0) {
                return NO_COLLISION;
            }
        }
        for (Lesson dodge : right.getDodgeableLessons()) {
            if (dodge.getPracticalBlocked().size() == 0 && dodge.getFwpmBlocked().size() == 0) {
                return NO_COLLISION;
            }
        }
        return COLLISION;
    }

    //Refactored PreferenceCollision
    public static boolean preferenceCollision(Lesson lesson, Preference preference) {
        if (lesson.getLecturerName().equals(preference.getLecturerName())
                && lesson.getDay() == preference.getDay()) {
            for (int i = 0; i < lesson.getBlockLength(); i++) {
                if (lesson.getPeriod().getHour() + i == preference.getHour()) {
                    return COLLISION;
                }
            }
        }
        return NO_COLLISION;
    }

    public static boolean softFWPMCollision(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (left.isFWPM() || right.isFWPM()) {
                if (!isFWPMCollision(left, right)) {
                    if (isAltCollision(left, right)) {
                        return COLLISION;
                    }
                }
            }
        }
        return NO_COLLISION;
    }

    private static boolean isTimeCollision(Lesson left, Lesson right) {
        int leftDay = left.getPeriod().getDay();
        int rightDay = right.getPeriod().getDay();

        int leftBlock = left.getBlockLength();
        int rightBlock = right.getBlockLength();

        int leftHour = left.getPeriod().getHour();
        int rightHour = right.getPeriod().getHour();

        if (leftDay == rightDay) {
            if (left.getId() != right.getId()) {
                if (!((leftHour + leftBlock - 1 < rightHour) || (leftHour > rightHour + rightBlock - 1))) {
                    return COLLISION;
                }

            }
        }
        return NO_COLLISION;
    }

    private static boolean isUKWCollision(Lesson left, Lesson right) {
        if (left.getUKWFlag() && right.getUKWFlag()) {
            return COLLISION;
        }
        return NO_COLLISION;
    }

    private static boolean isGKWCollision(Lesson left, Lesson right) {
        if (left.getGKWFlag() && right.getGKWFlag()) {
            return COLLISION;
        }
        return NO_COLLISION;
    }

    private static boolean isAltCollision(Lesson left, Lesson right) {
        if (left.getAltId() == 0 || right.getAltId() == 0) {
            return COLLISION;
        }
        if (left.getAltId() == 1 && right.getAltId() == 1) {
            return COLLISION;
        }
        if (left.getAltId() == 2 && right.getAltId() == 2) {
            return COLLISION;
        }

        return NO_COLLISION;
    }

    private static boolean isGroupCollision(Lesson left, Lesson right) {
        if (left.getGroupList().isEmpty() || right.getGroupList().isEmpty()) {
            return COLLISION;
        }
        for (String outer : left.getGroupList()) {
            for (String inner : right.getGroupList()) {
                if (outer.equals(inner)) {
                    return COLLISION;
                }
            }
        }
        return NO_COLLISION;
    }

    public static boolean getEndOfScheduleCollision(Lesson lesson, Period period) {
        if (lesson.getBlockLength() + period.getHour() - 1 > 12) {
            return COLLISION;
        }
        return NO_COLLISION;
    }

    //Soft Constraints
    public static int checkCompactness(Lesson lesson) {
        List<Integer> spacings = new ArrayList<>();
        //gets spacings for later Hours
        for (Lesson outer : lesson.getSameDay()) {
            spacings.add((lesson.getHour() + lesson.getBlockLength() - outer.getHour()));
        }
        //gets hour of following Lesson
        int MIN = Integer.MAX_VALUE;
        for (int temp : spacings) {
            if (temp >= 0 && temp < MIN) {
                MIN = temp;
            }
        }
        if (MIN == 0) {
            return 0;
        } else if (MIN == 1) {
            return -1;
        } else {
            return -2;
        }
    }

    public static boolean checkCapacity(Lesson lesson) {
        int capacity = 0;
        if (lesson.isFWPM()) {
            capacity = lesson.getFWPMStudents().size();
        } else if (lesson.getGroupList().isEmpty()) {
            capacity = lesson.getCourse().getSemester().getCapacity();
        } else if (lesson.getGroupList().size() > 0) {
            capacity = lesson.getCourse().getSemester().getCapacity() / 3;
        }
        if (lesson.getRoom().getCapacity() > capacity) {
            return NO_COLLISION;
        }
        return COLLISION;
    }


}
