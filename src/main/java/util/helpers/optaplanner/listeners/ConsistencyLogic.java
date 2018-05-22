package util.helpers.optaplanner.listeners;

import app.ScheduleSolution;
import domain.Lesson;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import sun.security.krb5.SCDynamicStoreConfig;
import util.ruleLogic.CollisionDetector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ConsistencyLogic {

    public static void updateSameDay(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson) {
        //removes the moved Lesson from all the lessons that knew it previously
        for (Lesson l : lesson.getSameDay()) {
            scoreDirector.beforeVariableChanged(l, "sameDay");
            l.getSameDay().remove(lesson);
            scoreDirector.afterVariableChanged(l, "sameDay");
        }
        List<Lesson> lessons = scoreDirector.getWorkingSolution().getLessons();
        List<Lesson> sameDay = new ArrayList<>();
        //iterate through all lessons
        for (Lesson l : lessons) {
            //check if another lesson has the same semester as the viewed lesson
            if (l.getSemesterName().equals(lesson.getSemesterName())) {
                //check if it takes place on the same dayhttps://open.spotify.com/track/0b93tWwuoAC0nXe1CfR30I
                if (l.getPeriod().getDay() == lesson.getPeriod().getDay()) {
                    //check if its not the same lesson
                    if (!l.equals(lesson)) {
                        //add it to temporary lesson list
                        sameDay.add(l);
                        scoreDirector.beforeVariableChanged(l, "sameDay");
                        //add the viewed lesson to the sameDay list of the other lesson
                        l.addSameDay(lesson);
                        scoreDirector.afterVariableChanged(l, "sameDay");
                    }
                }
            }
        }
        if (!new HashSet<>(sameDay).equals(new HashSet<>(lesson.getSameDay()))) {
            scoreDirector.beforeVariableChanged(lesson, "sameDay");
            //assign the temporary lesson list to the sameDay List
            lesson.setSameDay(sameDay);
            scoreDirector.afterVariableChanged(lesson, "sameDay");
        }
    }

    public static void updateFWPMBlockedList(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson) {
        removePreviousFWPMBlocked(scoreDirector, lesson);
        if (!lesson.isFWPM()) {
            for (Lesson outer : lesson.getSameDay()) {
                if (CollisionDetector.basicCollision(lesson, outer)) {
                    if (outer.isFWPM()) {
                        addFWPMBlockedLesson(scoreDirector, lesson, outer);
                    }
                }
            }
        }
    }

    private static void addFWPMBlockedLesson(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson, Lesson outer) {
        scoreDirector.beforeVariableChanged(lesson, "fwpmBlocked");
        lesson.getFwpmBlocked().add(outer);
        scoreDirector.afterVariableChanged(lesson, "fwpmBlocked");
    }

    private static void removePreviousFWPMBlocked(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson) {
        if (lesson.getFwpmBlocked().size() != 0) {
            Iterator<Lesson> outerIterator = lesson.getFwpmBlocked().iterator();
            while (outerIterator.hasNext()) {
                outerIterator.next();
                scoreDirector.beforeVariableChanged(lesson, "fwpmBlocked");
                outerIterator.remove();
                scoreDirector.afterVariableChanged(lesson, "fwpmBlocked");
            }
        }
    }

    public static void updatePracticalBlockedList1(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson) {
        for (Lesson outer : lesson.getSameSemester()) {
            scoreDirector.beforeVariableChanged(outer, "practicalBlocked");
            outer.getPracticalBlocked().remove(lesson);
            scoreDirector.afterVariableChanged(outer, "practicalBlocked");
        }
        removePreviousPracticalBlocked(scoreDirector, lesson);
        for (Lesson outer : lesson.getSameDay()) {
            if (CollisionDetector.basicCollision(lesson, outer)) {
                if (outer.isPractical()) {
                    addPracticalBlockedLesson(scoreDirector, lesson, outer);
                } else if (lesson.isPractical()) {
                    addPracticalBlockedLesson(scoreDirector, outer, lesson);
                } else if (lesson.isPractical() && outer.isPractical()) {
                    addPracticalBlockedLesson(scoreDirector, outer, lesson);
                    addPracticalBlockedLesson(scoreDirector, lesson, outer);
                }
            }
        }
    }

    public static void updatePracticalBlockedList(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson) {
        for (Lesson outer : lesson.getSameSemester()) {
            scoreDirector.beforeVariableChanged(outer, "practicalBlocked");
            outer.getPracticalBlocked().remove(lesson);
            scoreDirector.afterVariableChanged(outer, "practicalBlocked");
        }
        removePreviousPracticalBlocked(scoreDirector, lesson);
        for (Lesson outer : lesson.getSameDay()) {
            if (CollisionDetector.basicCollision(lesson, outer)) {
                if (lesson.isPractical() && outer.isPractical()) {
                    addPracticalBlockedLesson(scoreDirector, outer, lesson);
                    addPracticalBlockedLesson(scoreDirector, lesson, outer);
                }
            }
        }
    }

    private static void removePreviousPracticalBlocked(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson) {
        if (lesson.getPracticalBlocked().size() != 0) {
            Iterator<Lesson> outerIterator = lesson.getPracticalBlocked().iterator();
            while (outerIterator.hasNext()) {
                outerIterator.next();
                scoreDirector.beforeVariableChanged(lesson, "practicalBlocked");
                outerIterator.remove();
                scoreDirector.afterVariableChanged(lesson, "practicalBlocked");
            }
        }
    }

    private static void addPracticalBlockedLesson(ScoreDirector<ScheduleSolution> scoreDirector, Lesson lesson, Lesson outer) {
        scoreDirector.beforeVariableChanged(lesson, "practicalBlocked");
        lesson.getPracticalBlocked().add(outer);
        scoreDirector.afterVariableChanged(lesson, "practicalBlocked");
    }
}
