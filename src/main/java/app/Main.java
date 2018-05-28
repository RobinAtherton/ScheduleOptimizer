package app;

import data.output.ScheduleChangeLogger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import data.output.ScheduleTablePrinter;
import data.input.Initializer;
import data.output.ConstraintLogger;

import java.io.File;

public class Main {

    private static String SOLVER_CONFIG_XML = "solver/SolverConfig.xml";
    private static String OUTPUT_PATH = "src/main/resources/data/output/";
    private static int NORMAL_MODE = 0;
    private static int FILTERED_MODE = 1;

    public static void main(String[] args) throws Exception {
        ScheduleSolution scheduleSolution = new ScheduleSolution();
        Initializer.init(scheduleSolution, FILTERED_MODE);

        ScheduleSolution before = new ScheduleSolution(scheduleSolution);

        ConstraintLogger.logSolution();
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

        ScheduleSolution after = solve(scheduleSolution, solver);

        ScheduleChangeLogger comparator = new ScheduleChangeLogger(before, after);
        comparator.log();
    }

    private static ScheduleSolution solve (final ScheduleSolution scheduleSolution, Solver solver) throws Exception {
        ScheduleTablePrinter.printLessonsBySemester(scheduleSolution, new File(OUTPUT_PATH + "SchedulePreSolving.txt"));
        solver.solve(scheduleSolution);
        ScheduleSolution bestSolution = (ScheduleSolution) solver.getBestSolution();
        ScheduleTablePrinter.printLessonsBySemester(bestSolution, new File(OUTPUT_PATH + "SchedulePostSolving.txt"));
        ConstraintLogger.init(bestSolution);
        ConstraintLogger.logSolution();
        System.out.println(bestSolution.getScore());
        return bestSolution;
    }
}
