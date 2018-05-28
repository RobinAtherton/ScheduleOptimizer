package optaplanner.moves;

import app.ScheduleSolution;
import domain.Lesson;
import domain.Period;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import optaplanner.listeners.ConsistencyLogic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


public class ResistantSwapMove extends AbstractMove<ScheduleSolution> {

    private Lesson leftLesson;
    private Lesson rightLesson;

    public ResistantSwapMove(Lesson leftLesson, Lesson rightLesson) {
        this.leftLesson = leftLesson;
        this.rightLesson = rightLesson;
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<ScheduleSolution> scoreDirector) {
        return !Objects.equals(leftLesson, rightLesson);
    }

    @Override
    public ResistantSwapMove createUndoMove(ScoreDirector<ScheduleSolution> scoreDirector) {
        return new ResistantSwapMove(rightLesson, leftLesson);
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<ScheduleSolution> scoreDirector) {
        Period oldLeftPeriod = leftLesson.getPeriod();
        Period oldRightPeriod = rightLesson.getPeriod();

        scoreDirector.beforeVariableChanged(leftLesson, "period");
        leftLesson.setPeriod(oldRightPeriod);
        scoreDirector.afterVariableChanged(leftLesson, "period");
        ConsistencyLogic.updateSameDay(scoreDirector, leftLesson);

        scoreDirector.beforeVariableChanged(rightLesson, "period");
        rightLesson.setPeriod(oldLeftPeriod);
        scoreDirector.afterVariableChanged(rightLesson, "period");

        ConsistencyLogic.updateSameDay(scoreDirector, rightLesson);
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return Arrays.asList(leftLesson, rightLesson);
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Arrays.asList(leftLesson.getPeriod(), rightLesson.getPeriod());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof ResistantSwapMove) {
            ResistantSwapMove other = (ResistantSwapMove) o;
            return new EqualsBuilder()
                    .append(leftLesson, other.leftLesson)
                    .append(rightLesson, other.rightLesson)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(leftLesson)
                .append(rightLesson)
                .toHashCode();
    }

    @Override
    public String toString() {
        return leftLesson + " {" + leftLesson.getPeriod() + "} <-> "
                + rightLesson + " {" + rightLesson.getPeriod() + "}";
    }

}

