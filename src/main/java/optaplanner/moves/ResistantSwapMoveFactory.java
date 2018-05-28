package optaplanner.moves;

import app.ScheduleSolution;
import domain.Lesson;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.util.ArrayList;
import java.util.List;

public class ResistantSwapMoveFactory implements MoveListFactory<ScheduleSolution> {
    @Override
    public List<ResistantSwapMove> createMoveList(ScheduleSolution scheduleSolution) {
        List<Lesson> lessonList = scheduleSolution.getLessons();
        List<ResistantSwapMove> moveList = new ArrayList<>();
        for (Lesson left : lessonList) {
            for (Lesson right : lessonList) {
                moveList.add(new ResistantSwapMove(left, right));
            }
        }
        return moveList;
    }
}
