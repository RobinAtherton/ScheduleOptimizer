package util.helpers.optaplanner.moves;

import app.ScheduleSolution;
import domain.Lesson;
import domain.Period;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class CoupledLessonChangeMove extends AbstractMove<ScheduleSolution> {

    Lesson lesson;
    Period toPeriod;
    Period previousPeriod;

    public CoupledLessonChangeMove(Lesson lesson, Period period) {
        this.lesson = lesson;
        this.toPeriod = period;
        this.previousPeriod = period;
    }

    @Override
    protected AbstractMove<ScheduleSolution> createUndoMove(ScoreDirector<ScheduleSolution> scoreDirector) {
        return new CoupledLessonChangeMove(lesson, previousPeriod);
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<ScheduleSolution> scoreDirector) {
        previousPeriod = lesson.getPeriod();
        scoreDirector.beforeVariableChanged(lesson, "period");
        lesson.setPeriod(toPeriod);
        scoreDirector.afterVariableChanged(lesson, "period");
        for (Lesson coupled : lesson.getCoupledLessons()) {
            scoreDirector.beforeVariableChanged(coupled, "period");
            coupled.setPeriod(toPeriod);
            scoreDirector.afterVariableChanged(coupled, "period");
        }
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<ScheduleSolution> scoreDirector) {
        return !Objects.equals(previousPeriod, toPeriod);
    }

    @Override
    public Collection<?> getPlanningEntities() {
        return Collections.singletonList(lesson);
    }

    @Override
    public Collection<?> getPlanningValues() {
        return Collections.singletonList(toPeriod);
    }

    @Override
    public String toString() {
        return "Lesson: " + lesson.toString() + " at: " + lesson.getPeriod().toString() + " shifted to: " + toPeriod.toString() + "\n"
                + " previousPeriod: " + previousPeriod.toString();
    }
}
