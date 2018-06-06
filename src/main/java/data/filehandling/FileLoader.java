package data.filehandling;

import data.processing.XMLConverter;

import java.io.File;
import java.io.IOException;

public class FileLoader {

    private static final int AMOUNT_INPUT_FILES = 7;
    private static final int AMOUNT_MODIFIED_FILES = 5;
    private static final int AMOUNT_FILTERED_FILES = 8;
    private static final int AMOUNT_OCCUPANCY_FILES = 3;
    private static final int AMOUNT_OCC_FILES = 1;

    private static final int AMOUNT_NEEDED_FILES = AMOUNT_INPUT_FILES + AMOUNT_OCC_FILES;
    private static final String[] SEMESTERS = {"I4"};

    //folder Paths
    private static String INPUT_PATH = "src/main/resources/data/input/original/";
    private static String REFACTORED_PATH = "src/main/resources/data/input/refactored/";
    private static String FILTERED_PATH = "src/main/resources/util/data/filtered/";
    private static String MODIFIED_PATH = "src/main/resources/util/data/modified/";
    private static String FWPM_PATH = "src/main/resources/data/input/fwpm/";

    //input Paths
    private static String SUBJECTS_PATH = INPUT_PATH + "Subjects.csv";
    private static String LESSONS_PATH = INPUT_PATH + "Lessons.csv";
    private static String LECTURERS_PATH = INPUT_PATH + "Lecturers.csv";
    private static String PREFEERENCES_PATH = INPUT_PATH + "Preferences.csv";
    private static String ROOMS_PATH = INPUT_PATH + "Rooms.csv";
    private static String SEMESTERS_PATH = INPUT_PATH + "Semesters.csv";
    private static String LESSON_COLLECTIONS_PATH = INPUT_PATH + "LessonCollections.csv";

    private static String REFACTORED_SUBJECTS_PATH = REFACTORED_PATH + "Subjects.csv";
    private static String REFACTORED_LESSONS_PATH = REFACTORED_PATH + "Lessons.csv";
    private static String REFACTORED_LECTURERS_PATH = REFACTORED_PATH + "Lecturers.csv";
    private static String REFACTORED_PREFEERENCES_PATH = REFACTORED_PATH + "Preferences.csv";
    private static String REFACTORED_ROOMS_PATH = REFACTORED_PATH + "Rooms.csv";
    private static String REFACTORED_SEMESTERS_PATH = REFACTORED_PATH + "Semesters.csv";
    private static String REFACTORED_LESSON_COLLECTIONS_PATH = REFACTORED_PATH + "LessonCollections.csv";

    //input Files
    private static File subjectsFile;
    private static File lessonsFile;
    private static File lecturersFile;
    private static File preferencesFile;
    private static File roomsFile;
    private static File semestersFile;
    private static File lessonsCollectionFile;

    //occupancy Paths
    private static String FWPM_OCCUPANCY_PATH = FWPM_PATH + "Occupancy.csv";
    private static String FWPM_OCCUPANCY_XML_PATH = FWPM_PATH + "Occupancy.xml";
    private static String FWPM_OCCUPANCY_ASSERTED_PATH = FWPM_PATH + "AssertedOccupancy.csv";

    //occupancy Files
    private static File FWPMOccupancyFile;
    private static File FWPMOccupancyXML;
    private static File FWPMAssertedOccupancyFile;

    //Filtered Paths
    private static String FILTERED_SEMESTERS_PATH = FILTERED_PATH + "FilteredSemesters.csv";
    private static String FILTERED_LESSON_COLLECTIONS_PATH = FILTERED_PATH + "FilteredLessonCollections.csv";
    private static String FILTERED_UKW_LESSONS_PATH = FILTERED_PATH + "FilteredUKWLessons.csv";
    private static String FILTERED_GKW_LESSONS_PATH = FILTERED_PATH + "FilteredGKWLessons.csv";
    private static String FILTERED_ALT_LESSONS_PATH = FILTERED_PATH + "FilteredAltLessons.csv";
    private static String FILTERED_GROUPED_LESSONS_PATH = FILTERED_PATH + "FilteredGroupedLessons.csv";
    private static String FILTERED_LESSONS_PATH = FILTERED_PATH + "FilteredLessons.csv";
    private static String FILTERED_FWPM_LESSONS_PATH = FILTERED_PATH + "FilteredFWPMLessons.csv";

    //Filtered Files
    private static File filteredSemestersFile;
    private static File filteredLessonCollectionsFile;
    private static File filteredUKWLessonsFile;
    private static File filteredGroupedLessonsFile;
    private static File filteredGKWLessonsFile;
    private static File filteredAltLessonsFile;
    private static File filteredLessonsFile;
    private static File filteredFWPMLessonsFile;

    //Modified Paths
    private static String MODIFIED_UKW_LESSONS_PATH = MODIFIED_PATH + "ModifiedUKWLessons.csv";
    private static String MODIFIED_GKW_LESSONS_PATH = MODIFIED_PATH + "ModifiedGKWLessons.csv";
    private static String MODIFIED_ALT_LESSONS_PATH = MODIFIED_PATH + "ModifiedAltLessons.csv";
    private static String MODIFIED_GROUPED_LESSONS_PATH = MODIFIED_PATH + "ModifiedGroupedLessons.csv";
    private static String MODIFIED_FWPM_LESSONS_PATH = MODIFIED_PATH + "ModifiedFWPMLessons.csv";

    //Modified Files
    private static File modifiedUKWLessonsFile;
    private static File modifiedGKWLessonsFile;
    private static File modifiedAltLessonsFile;
    private static File modifiedGroupedLessonsFile;
    private static File modifiedFWPMLessonsFile;

    public static void load() throws Exception {
        createRefactoredFiles();
        logFileCreation();
        loadFiles();
        createFilteredFiles(SEMESTERS);
        createModifiedFiles();
        createFWPMFile();
    }

    private static void logFileCreation() throws Exception {
        File f1 = new File(FILTERED_SEMESTERS_PATH);
        File f2 = new File(FILTERED_LESSON_COLLECTIONS_PATH);
        File f3 = new File(FILTERED_UKW_LESSONS_PATH);
        File f4 = new File(FILTERED_GKW_LESSONS_PATH);
        File f5 = new File(FILTERED_ALT_LESSONS_PATH);
        File f6 = new File(FILTERED_GROUPED_LESSONS_PATH);
        File f7 = new File(FILTERED_LESSONS_PATH);
        File f8 = new File(FILTERED_FWPM_LESSONS_PATH);

        File mf1 = new File(MODIFIED_UKW_LESSONS_PATH);
        File mf2 = new File(MODIFIED_GKW_LESSONS_PATH);
        File mf3 = new File(MODIFIED_ALT_LESSONS_PATH);
        File mf4 = new File(MODIFIED_GROUPED_LESSONS_PATH);
        File mf5 = new File(MODIFIED_FWPM_LESSONS_PATH);

        File o1 = new File(FWPM_OCCUPANCY_PATH);
        File o2 = new File(FWPM_OCCUPANCY_XML_PATH);
        File o3 = new File(FWPM_OCCUPANCY_ASSERTED_PATH);

        File[] f = {f1, f2, f3, f4, f5, f6, f7, f8};
        File[] m = {mf1, mf2, mf3, mf4, mf5};
        File[] o = {o1, o2, o3};
        if (f.length > AMOUNT_FILTERED_FILES) {
            throw new Exception("Too many Filtered Semester Paths");
        }
        if (m.length > AMOUNT_MODIFIED_FILES) {
            throw new Exception("Too many Modified Semester Paths");
        }
        if (o.length > AMOUNT_OCCUPANCY_FILES) {
            throw new Exception("Too many Occupancy Paths");
        }

        System.out.println("Filtered Files:");
        for (File x : f) {
            if (x.createNewFile()) {
                System.out.println("\t" + x.getName() + " created.");
            } else {
                System.out.println("\t" + x.getName() + " exists.");
            }
        }
        System.out.println("\n");
        System.out.println("Modified Files");
        for (File y : m) {
            if (y.createNewFile()) {
                System.out.println("\t" + y.getName() + " created.");
            } else {
                System.out.println("\t" + y.getName() + " exists.");
            }
        }
        System.out.println("\n");
        System.out.println("Occupancy Files");
        for (File z : o) {
            if (z.createNewFile()) {
                System.out.println("\t" + z.getName() + " created.");
            } else {
                System.out.println("\t" + z.getName() + " exists.");
            }
        }
        System.out.println("\n");

     }

    private static void loadFiles() {
        String[] names = new String[AMOUNT_NEEDED_FILES];
        names[0] = REFACTORED_SUBJECTS_PATH;
        names[1] = REFACTORED_LESSONS_PATH;
        names[2] = REFACTORED_LECTURERS_PATH;
        names[3] = REFACTORED_PREFEERENCES_PATH;
        names[4] = REFACTORED_ROOMS_PATH;
        names[5] = REFACTORED_SEMESTERS_PATH;
        names[6] = REFACTORED_LESSON_COLLECTIONS_PATH;
        names[7] = FWPM_OCCUPANCY_XML_PATH;

        File[] files = new File[AMOUNT_NEEDED_FILES];
        for (int i = 0; i < AMOUNT_NEEDED_FILES; i++) {
            File file = new File(names[i]);
            files[i] = file;
        }
        subjectsFile = files[0];
        lessonsFile = files[1];
        lecturersFile = files[2];
        preferencesFile = files[3];
        roomsFile = files[4];
        semestersFile = files[5];
        lessonsCollectionFile = files[6];
        FWPMOccupancyXML = files[7];
    }

    private static void createFilteredFiles(String... semesters) throws IOException {
        FileGenerator.writeFilteredSemesters(FILTERED_SEMESTERS_PATH, getSemestersFile() , semesters);
        FileGenerator.writeSemesterFilteredLessonCollections(FILTERED_LESSON_COLLECTIONS_PATH, getLessonCollectionsFile(), semesters);
        FileGenerator.writeFilteredUKWLessons(FILTERED_UKW_LESSONS_PATH, getLessonCollectionsFile(), semesters);
        FileGenerator.writeFilteredGKWLessons(FILTERED_GKW_LESSONS_PATH, getLessonCollectionsFile(), semesters);
        FileGenerator.writeFilteredGroupedLessons(FILTERED_GROUPED_LESSONS_PATH, getLessonCollectionsFile(), semesters);
        FileGenerator.writeFilteredAltLessons(FILTERED_ALT_LESSONS_PATH, getLessonCollectionsFile(), semesters);
        FileGenerator.writeFilteredSemesterLessons(FILTERED_LESSONS_PATH, getLessonsFile(), semesters);
        FileGenerator.writeFilteredFWPMLessons(FILTERED_FWPM_LESSONS_PATH, getLessonCollectionsFile(), semesters);

        String[] names = new String[AMOUNT_FILTERED_FILES];
        names[0] = FILTERED_SEMESTERS_PATH;
        names[1] = FILTERED_LESSON_COLLECTIONS_PATH;
        names[2] = FILTERED_UKW_LESSONS_PATH;
        names[3] = FILTERED_GROUPED_LESSONS_PATH;
        names[4] = FILTERED_GKW_LESSONS_PATH;
        names[5] = FILTERED_ALT_LESSONS_PATH;
        names[6] = FILTERED_LESSONS_PATH;
        names[7] = FILTERED_FWPM_LESSONS_PATH;

        File[] files = new File[AMOUNT_FILTERED_FILES];
        for (int i = 0; i < AMOUNT_FILTERED_FILES; i++) {
            File file = new File(names[i]);
            files[i] = file;
        }
        filteredSemestersFile = files[0];
        filteredLessonCollectionsFile = files[1];
        filteredUKWLessonsFile = files[2];
        filteredGroupedLessonsFile = files[3];
        filteredGKWLessonsFile = files[4];
        filteredAltLessonsFile = files[5];
        filteredLessonsFile = files[6];
        filteredFWPMLessonsFile = files[7];
    }

    private static void createModifiedFiles() throws IOException {
        FileGenerator.writeModifiedUKWLessons(MODIFIED_UKW_LESSONS_PATH, getLessonCollectionsFile());
        FileGenerator.writeModifiedGKWLessons(MODIFIED_GKW_LESSONS_PATH, getLessonCollectionsFile());
        FileGenerator.writeModifiedGroupedLessons(MODIFIED_GROUPED_LESSONS_PATH, getLessonCollectionsFile());
        FileGenerator.writeModifiedAltLessons(MODIFIED_ALT_LESSONS_PATH, getLessonCollectionsFile());
        FileGenerator.writeModifiedFWPMLessons(MODIFIED_FWPM_LESSONS_PATH, getLessonCollectionsFile());

        String[] names = new String[AMOUNT_MODIFIED_FILES];
        names[0] = MODIFIED_UKW_LESSONS_PATH;
        names[1] = MODIFIED_GROUPED_LESSONS_PATH;
        names[2] = MODIFIED_GKW_LESSONS_PATH;
        names[3] = MODIFIED_ALT_LESSONS_PATH;
        names[4] = MODIFIED_FWPM_LESSONS_PATH;

        File[] files = new File[AMOUNT_MODIFIED_FILES];
        for (int i = 0; i < AMOUNT_MODIFIED_FILES; i++) {
            File file = new File(names[i]);
            files[i] = file;
        }
        modifiedUKWLessonsFile = files[0];
        modifiedGroupedLessonsFile = files[1];
        modifiedGKWLessonsFile = files[2];
        modifiedAltLessonsFile = files[3];
        modifiedFWPMLessonsFile = files[4];
    }

    private static void createRefactoredFiles() throws Exception {
        FileGenerator.reformat(SEMESTERS_PATH, REFACTORED_SEMESTERS_PATH);
        FileGenerator.reformat(SUBJECTS_PATH, REFACTORED_SUBJECTS_PATH);
        FileGenerator.reformat(LECTURERS_PATH, REFACTORED_LECTURERS_PATH);
        FileGenerator.reformat(PREFEERENCES_PATH, REFACTORED_PREFEERENCES_PATH);
        FileGenerator.reformat(ROOMS_PATH, REFACTORED_ROOMS_PATH);
        FileGenerator.reformat(LESSON_COLLECTIONS_PATH, REFACTORED_LESSON_COLLECTIONS_PATH);
        FileGenerator.reformat(LESSONS_PATH, REFACTORED_LESSONS_PATH);
    }

    private static void createFWPMFile() throws IOException {
        FWPMOccupancyFile = XMLConverter.convert(getFWPMOccupancyXML(), FWPM_OCCUPANCY_PATH);
        FWPMAssertedOccupancyFile = XMLConverter.assertShortNames(getFWPMOccupancyFile(), getSubjectsFile(), FWPM_OCCUPANCY_ASSERTED_PATH);
    }

    public static File getSubjectsFile() {
        return subjectsFile;
    }

    public static File getLessonsFile() {
        return lessonsFile;
    }

    public static File getLecturersFile() {
        return lecturersFile;
    }

    public static File getPreferencesFile() {
        return preferencesFile;
    }

    public static File getRoomsFile() {
        return roomsFile;
    }

    public static File getSemestersFile() {
        return semestersFile;
    }

    public static File getLessonCollectionsFile() {
        return lessonsCollectionFile;
    }

    public static File getFilteredSemestersFile() {
        return filteredSemestersFile;
    }

    public static File getFilteredLessonCollectionsFile() {
        return filteredLessonCollectionsFile;
    }

    public static File getFilteredUKWLessonsFile() {
        return filteredUKWLessonsFile;
    }

    public static File getModifiedUKWLessonsFile() {
        return modifiedUKWLessonsFile;
    }

    public static File getFilteredGroupedLessonsFile() {
        return filteredGroupedLessonsFile;
    }

    public static File getModifiedGroupedLessonsFile() {
        return modifiedGroupedLessonsFile;
    }

    public static File getFilteredGKWLessonsFile() {
        return filteredGKWLessonsFile;
    }

    public static File getFilteredAltLessonsFile() {
        return filteredAltLessonsFile;
    }

    public static File getModifiedGKWLessonsFile() {
        return modifiedGKWLessonsFile;
    }

    public static File getModifiedAltLessonsFile() {
        return modifiedAltLessonsFile;
    }

    public static File getFilteredFWPMLessonsFile() {
        return filteredFWPMLessonsFile;
    }

    public static File getModifiedFWPMLessonsFile() {
        return modifiedFWPMLessonsFile;
    }

    public static File getFWPMOccupancyFile() {
        return FWPMOccupancyFile;
    }

    public static File getFWPMOccupancyXML() {
        return FWPMOccupancyXML;
    }

    public static File getFWPMAssertedOccupancyFile() {
        return FWPMAssertedOccupancyFile;
    }
}
