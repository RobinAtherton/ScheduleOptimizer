package util.helpers.optaplanner.moves;

import app.ScheduleSolution;
import domain.Lesson;
import domain.Period;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import util.ruleLogic.CollisionDetector;

import java.util.ArrayList;
import java.util.List;

public class CoupledLessonChangeMoveFactory implements MoveListFactory<ScheduleSolution> {
    @Override
    public List<CoupledLessonChangeMove> createMoveList(ScheduleSolution scheduleSolution) {
        List<Lesson> lessonList = scheduleSolution.getLessons();
        List<Period> periodList = scheduleSolution.getPeriods();
        List<CoupledLessonChangeMove> moveList = new ArrayList<>();
        for (Lesson left : lessonList) {
            for (Period right : periodList) {
                if (!CollisionDetector.getEndOfScheduleCollision(left, right)) {
                    moveList.add(new CoupledLessonChangeMove(left, right));
                }
            }
        }
        return moveList;
    }
}
