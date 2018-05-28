package optaplanner.filters;

import app.ScheduleSolution;
import domain.Lesson;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class SamePeriodSwapMoveFilter implements SelectionFilter<ScheduleSolution, SwapMove> {

    @Override
    public boolean accept(ScoreDirector<ScheduleSolution> scoreDirector, SwapMove move) {
        Lesson left = (Lesson) move.getLeftEntity();
        Lesson right = (Lesson) move.getRightEntity();
        return (left.getPeriod().getHour() != right.getPeriod().getHour()) && (left.getPeriod().getDay() != right.getPeriod().getDay()) && (left.getRoom() != right.getRoom());
     }
}
