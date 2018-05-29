package logic;

import domain.Lesson;
import domain.Period;
import domain.Preference;
import domain.Student;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {

    private static boolean COLLISION = true;
    private static boolean NO_COLLISION = false;

    //Refactored FWPM collision
    public static boolean extensiveCollision(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (!left.isFWPM() && !right.isFWPM()) {
                return baseCollision(left, right);
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

    private static boolean mustToFwpmCollision(Lesson left, Lesson right) {
        boolean nonBlockedAvailable = false;
        if (right.isFWPM() && !left.isFWPM()) {
            for (Lesson dodge : left.getDodgeableLessons()) {
                if (dodge.getFwpmBlocked().size() == 0) {
                    nonBlockedAvailable = true;
                    break;
                }
            }
        }
        if (nonBlockedAvailable) {
            return NO_COLLISION;
        }
        left.setCollisionReason("FWPM - Pflicht Kollision");
        right.setCollisionReason("FWPM - Pflicht Kollision");
        return COLLISION;
    }

    private static boolean fwpmToMustCollision(Lesson left, Lesson right) {
        boolean nonBlockedAvailable = false;
        if (!right.isFWPM() && left.isFWPM()) {
            for (Lesson dodge : left.getDodgeableLessons()) {
                if (dodge.getFwpmBlocked().size() == 0) {
                    nonBlockedAvailable = true;
                    break;
                }
            }
        }
        if (nonBlockedAvailable) {
            return NO_COLLISION;
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

    //TODO Review
    //Refactored Collision Code--------------------------------------------TO IMPLEMENT----------------------------------------------------------
    public static boolean isColliding(Lesson left, Lesson right) {
        if (!left.isFWPM() && !right.isFWPM()) {
            return isMustLessonCollision(left, right);
        } else if (left.isFWPM() ^ right.isFWPM()) {
            if (isGroupCollision(left, right)) { //check if necessary
                if (isBiWeeklyCollision(left, right)) {
                    return isMustFwpmLessonCollision(left, right) ^ isMustFwpmLessonCollision(right, left);
                }
            }
        } else {
            if (isGroupCollision(left, right)) {
                if (isBiWeeklyCollision(left, right)) {
                    return isFwpmLessonCollision(left, right);
                }
            }
        }
        return NO_COLLISION;
    }

    private static boolean isMustLessonCollision(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (isGroupCollision(left, right)) {
                if (isBiWeeklyCollision(left, right)) {
                    return COLLISION;
                }
            }
        }
        return NO_COLLISION;
    }

    private static boolean isMustFwpmLessonCollision(Lesson left, Lesson right) {
        boolean nonBlockedAvailable = false;
        if (isTimeCollision(left, right)) {
            if (right.isFWPM() && !left.isFWPM()) {
                for (Lesson dodge : left.getDodgeableLessons()) {
                    if (dodge.getFwpmBlocked().size() == 0) {
                        nonBlockedAvailable = true;
                        break;
                    }
                }
            }
            if (nonBlockedAvailable) {
                return NO_COLLISION;
            }
        }
        return COLLISION;
    }

    private static boolean isFwpmLessonCollision(Lesson left, Lesson right) {
        if (isTimeCollision(left, right)) {
            if (left.isFWPM() && right.isFWPM()) {
                for (Student leftStudent : left.getFWPMStudents()) {
                    for (Student rightStudent : right.getFWPMStudents()) {
                        if (leftStudent.equals(rightStudent)) {
                            return COLLISION;
                        }
                    }
                }
            }
        }
        return NO_COLLISION;
    }

    //Wabern


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


}
