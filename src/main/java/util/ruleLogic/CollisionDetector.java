package util.ruleLogic;

import domain.Lesson;
import domain.Period;
import domain.Preference;
import domain.Student;
import org.apache.commons.math3.genetics.NPointCrossover;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {

    private static boolean COLLISION = true;
    private static boolean NO_COLLISION = false;


    //Refactored FWPM collision
    public static boolean extensiveCollision(Lesson left, Lesson right) {
        if (timedCollision(left, right)) {
            if (!left.isFWPM() && !right.isFWPM()) {
                if (baseCollision(left, right)) {
                    if (isPracticalCollision(left, right)) {
                        return NO_COLLISION;
                    } else {
                        return COLLISION;
                    }
                }
            } else if (left.isFWPM() ^ right.isFWPM()) {
                if (baseCollision(left, right)) {
                    return fwpToNonCollision(left, right);
                }
            } else {
                if (baseCollision(left, right)) {
                    return isFWPMCollision(left, right);
                }
            }
        }
        return NO_COLLISION;
    }

    public static boolean noSameLessonsApartOnSameDay(Lesson lesson) {
        for (Lesson outer : lesson.getSameDay()) {
            if (!lesson.equals(outer)) {
                if (!adjacent(lesson, outer) && lesson.getSubjectName().equals(outer.getSubjectName())) {
                    return COLLISION;
                }
            }
        }
        return NO_COLLISION;
    }

    private static boolean adjacent(Lesson left, Lesson right) {
       if (left.getHour()+left.getBlockLength() == right.getHour()
               || right.getHour()+right.getBlockLength() == left.getHour()) {
            return true;
       }
       return false;
    }

    private static boolean checkForCapacity(Lesson left, Lesson right) {
        int leftSize = left.getCourse().getSemester().getCapacity();
        int rightSize = right.getCourse().getSemester().getCapacity();
        if (left.getGroupList().size() > 0) {
            leftSize = leftSize / 3;
        }
        if (right.getGroupList().size() > 0) {
            rightSize = rightSize / 3;
        }
        if (left.isFWPM()) {
            leftSize = left.getFWPMStudents().size();
        }
        if (right.isFWPM()) {
            rightSize = right.getFWPMStudents().size();
        }
        if (leftSize + rightSize > left.getCourse().getSemester().getCapacity()
                || leftSize + rightSize > right.getCourse().getSemester().getCapacity()) {
            return COLLISION;
        }
        return NO_COLLISION;
    }

    private static boolean isPracticalCollision(Lesson left, Lesson right) {
        boolean nonBlockedAvailable = false;
        if (left.isPractical() && right.isPractical()) {
            for (Lesson dodge : left.getDodgeableLessons()) {
                if (dodge.getPracticalBlocked().size() == 0) {
                    nonBlockedAvailable = true;
                    break;
                }
            }
            for (Lesson dodge : right.getDodgeableLessons()) {
                if (dodge.getPracticalBlocked().size() == 0) {
                    nonBlockedAvailable = true;
                    break;
                }
            }
        }
        return nonBlockedAvailable;
    }

    private static boolean fwpToNonCollision(Lesson left, Lesson right) {
        boolean nonBlockedAvailable = false;
        if (left.isFWPM()) {
            for (Lesson dodge : right.getDodgeableLessons()) {
                if (dodge.getFwpmBlocked().size() == 0) {
                    nonBlockedAvailable = true;
                    break;
                }
            }
            if (nonBlockedAvailable) {
                return NO_COLLISION;
            } else {
                left.setCollisionReason("FWPM-NON_FWPM");
                right.setCollisionReason("FWPM-NON_FWPM");
            }
        } else if (right.isFWPM()) {
            for (Lesson dodge : left.getDodgeableLessons()) {
                if (dodge.getFwpmBlocked().size() == 0) {
                    nonBlockedAvailable = true;
                    break;
                }
            }
            if (nonBlockedAvailable) {
                return NO_COLLISION;
            } else {
                left.setCollisionReason("FWPM-NON_FWPM");
                right.setCollisionReason("FWPM-NON_FWPM");
            }
        } else {

        }
        return COLLISION;        //TODO FIX SCORE CORRUPTION
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

    public static boolean basicCollision(Lesson left, Lesson right) {
        if (timedCollision(left, right)) {
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

    //TODO Review PreferenceCollision
    public static boolean getPreferenceCollision(Lesson lesson, Preference preference) {
        if (isPreferenceBlockColliding(lesson, preference)) {
            return COLLISION;
        }
        return NO_COLLISION;
    }

    //Refactored PreferenceCollision
    private static boolean isPreferenceBlockColliding(Lesson lesson, Preference preference) {
        if (lesson.getLecturerName().equals(preference.getLecturer().getShortName())
                && lesson.getDay() == preference.getDay()) {
            for (int i = 0; i < lesson.getBlockLength(); i++) {
                if (lesson.getPeriod().getHour() + i == preference.getHour()) {
                    return COLLISION;
                }
            }
        }
        return NO_COLLISION;
    }

    private static boolean baseCollision(Lesson left, Lesson right) {
        if (isGroupCollision(left, right)) {
            if (isUKWCollision(left, right)) {
                left.setCollisionReason("UKW");
                return COLLISION;
            }
            if (isGKWCollision(left, right)) {
                left.setCollisionReason("GKW");
                return COLLISION;
            }
            if (isAltCollision(left, right)) {
                left.setCollisionReason("ALT");
                return COLLISION;
            }
        }
        return NO_COLLISION;
    }

    public static boolean softFWPMCollision(Lesson left, Lesson right) {
        if (timedCollision(left, right)) {
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

    private static boolean timedCollision(Lesson left, Lesson right) {
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


}
