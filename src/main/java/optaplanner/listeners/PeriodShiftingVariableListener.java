package optaplanner.listeners;

import domain.Lesson;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class PeriodShiftingVariableListener implements VariableListener<Lesson> {

    @Override
    public boolean requiresUniqueEntityEvents() {
        return true;
    }

    @Override
    public void beforeEntityAdded(ScoreDirector scoreDirector, Lesson lesson) {
    }

    @Override
    public void afterEntityAdded(ScoreDirector scoreDirector, Lesson lesson) {
        //do nothing
    }

    @Override
    public void beforeVariableChanged(ScoreDirector scoreDirector, Lesson lesson) {
    }

    @Override
    public void afterVariableChanged(ScoreDirector scoreDirector, Lesson lesson) {
        ConsistencyLogic.updateSameDay(scoreDirector, lesson);
        ConsistencyLogic.updateFWPMBlockedList(scoreDirector, lesson);
        ConsistencyLogic.updatePracticalBlockedList(scoreDirector, lesson);
       // ConsistencyLogic.updateGroupBlockedList(scoreDirector, lesson);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector scoreDirector, Lesson lesson) {
        //do nothing
    }

    @Override
    public void afterEntityRemoved(ScoreDirector scoreDirector, Lesson lesson) {
        //do nothing
    }
}