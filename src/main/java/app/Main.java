package app;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.data.output.ScheduleOutput;
import util.helpers.own.logic.Initializer;
import util.helpers.own.logic.SolutionTester;

import java.io.File;

public class Main {

    private static String SOLVER_CONFIG_XML = "solver/SolverConfig.xml";
    private static String OUTPUT_PATH = "src/main/resources/util/data/output/";
    private static int NORMAL_MODE = 0;
    private static int FILTERED_MODE = 1;

    public static void main(String[] args) throws Exception {
        ScheduleSolution scheduleSolution = new ScheduleSolution();
        Initializer.init(scheduleSolution, NORMAL_MODE );
        SolutionTester.logSolution();
        final Solver solver = SolverFactory.createFromXmlResource(SOLVER_CONFIG_XML).buildSolver();
        ScoreDirectorFactory scoreDirectorFactory = solver.getScoreDirectorFactory();
        ScoreDirector scoreDirector = scoreDirectorFactory.buildScoreDirector();

        solver.addEventListener(new SolverEventListener<ScheduleSolution>() {
            @Override
            public void bestSolutionChanged(BestSolutionChangedEvent<ScheduleSolution> event) {
                if (event.getNewBestSolution().getScore().isFeasible()) {
                    System.out.println("############## solution candidate #############");
                    System.out.println(event.getNewBestSolution());
                    System.out.println("Current Hard score: " + event.getNewBestSolution().getScore().getHardScore());
                    System.out.println("Current Soft score: " + event.getNewBestSolution().getScore().getSoftScore());
                    System.out.println("###############################################\n\n");
                }
            }
        });

        solve(scheduleSolution, solver);
    }

    private static void solve(final ScheduleSolution scheduleSolution, Solver solver) throws Exception {
        ScheduleOutput.printScheduleToFile(scheduleSolution, new File(OUTPUT_PATH + "SchedulePreSolving.txt"));
        ScheduleOutput.printLessonsBySemester(scheduleSolution, new File(OUTPUT_PATH + "LessonsPreSolving.txt"));
        solver.solve(scheduleSolution);
        ScheduleSolution bestSolution = (ScheduleSolution) solver.getBestSolution();
        ScheduleOutput.printScheduleToFile(bestSolution, new File(OUTPUT_PATH + "SchedulePostSolving.txt"));
        ScheduleOutput.printLessonsBySemester(bestSolution, new File(OUTPUT_PATH + "LessonsPostSolving.txt"));
        SolutionTester.init(bestSolution);
        SolutionTester.logSolution();
        System.out.println(bestSolution.getScore());
    }


}
