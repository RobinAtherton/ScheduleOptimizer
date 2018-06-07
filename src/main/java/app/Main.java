package app;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import data.output.ScheduleChangeLogger;
import data.output.UntisExporter;
import org.drools.core.spi.Constraint;
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
    private static String REFACTORED_PATH = "src/main/resources/data/input/refactored/";

    private static String INPUT_PATH = REFACTORED_PATH + "Lessons.csv";

    private static String SCHEDULE_PRE_SOLVING = OUTPUT_PATH + "SchedulePreSolving.txt";
    private static String SCHEDULE_POST_SOLVING = OUTPUT_PATH + "SchedulePostSolving.txt";

    private static String UNTIS_EXPORT_PRE = OUTPUT_PATH + "PreLessons.txt";
    private static String UNTIS_EXPORT_POST = OUTPUT_PATH + "Lessons.txt";

    private static int NORMAL_MODE = 0;
    private static int FILTERED_MODE = 1;

    public static void main(String[] args) throws Exception {
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
        ScheduleSolution scheduleSolution = new ScheduleSolution();
        Initializer.init(scheduleSolution, NORMAL_MODE);

        ScheduleSolution before = new ScheduleSolution(scheduleSolution);
        log(before, SCHEDULE_PRE_SOLVING, UNTIS_EXPORT_PRE);
        System.out.println("------------------------------------------------------------------------------------------------------");
        solver.solve(scheduleSolution);
        ScheduleSolution after = (ScheduleSolution) solver.getBestSolution();
        log(after, SCHEDULE_POST_SOLVING, UNTIS_EXPORT_POST);
        System.out.println(after.getScore());
        System.out.println("------------------------------------------------------------------------------------------------------");

        ScheduleChangeLogger comparator = new ScheduleChangeLogger(before, after);
        comparator.log();
    }

    private static ScheduleSolution solve (final ScheduleSolution scheduleSolution, Solver solver) throws Exception {
        //Pre Phase
        ScheduleTablePrinter.printLessonsBySemester(scheduleSolution, new File(SCHEDULE_PRE_SOLVING));
        UntisExporter.export(scheduleSolution.getLessons(), INPUT_PATH, UNTIS_EXPORT_PRE);
        ConstraintLogger.init(scheduleSolution);
        ConstraintLogger.logSolution();

        //Solving Phase
        solver.solve(scheduleSolution);
        ScheduleSolution bestSolution = (ScheduleSolution) solver.getBestSolution();

        //Post Phase
        ScheduleTablePrinter.printLessonsBySemester(bestSolution, new File(SCHEDULE_POST_SOLVING));
        UntisExporter.export(bestSolution.getLessons(), INPUT_PATH, UNTIS_EXPORT_POST);
        ConstraintLogger.init(bestSolution);
        ConstraintLogger.logSolution();
        System.out.println(bestSolution.getScore());
        return bestSolution;
    }

    private static void log(final ScheduleSolution scheduleSolution, String schedulePath, String untisExport) throws Exception {
        ScheduleTablePrinter.printLessonsBySemester(scheduleSolution, new File(schedulePath));
        UntisExporter.export(scheduleSolution.getLessons(), INPUT_PATH, untisExport);
        ConstraintLogger.init(scheduleSolution);
        ConstraintLogger.logSolution();
    }
}
