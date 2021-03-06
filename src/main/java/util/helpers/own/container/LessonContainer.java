package util.helpers.own.container;

import domain.Lesson;

import java.util.*;

public class LessonContainer {


    private Map<Integer, Map<Integer, List<Lesson>>> schedule = new HashMap<>();

    private static int SCHEDULE_LENGTH = 12;
    private static int MONDAY = 1;
    private static int TUESDAY = 2;
    private static int WEDNESDAY = 3;
    private static int THURSDAY = 4;
    private static int FRIDAY = 5;

    public LessonContainer() {
        schedule.put(MONDAY, new HashMap<Integer, List<Lesson>>());
        schedule.put(TUESDAY, new HashMap<Integer, List<Lesson>>());
        schedule.put(WEDNESDAY, new HashMap<Integer, List<Lesson>>());
        schedule.put(THURSDAY, new HashMap<Integer, List<Lesson>>());
        schedule.put(FRIDAY, new HashMap<Integer, List<Lesson>>());
        for (int i = 1; i <= 12; i++) {
            schedule.get(MONDAY).put(i, new ArrayList<Lesson>());
            schedule.get(TUESDAY).put(i, new ArrayList<Lesson>());
            schedule.get(WEDNESDAY).put(i, new ArrayList<Lesson>());
            schedule.get(THURSDAY).put(i, new ArrayList<Lesson>());
            schedule.get(FRIDAY).put(i, new ArrayList<Lesson>());
        }
    }

    public void add(Lesson lesson) {
        List<Lesson> temp = schedule.get(lesson.getPeriod().getDay()).get(lesson.getPeriod().getHour());
        temp.add(lesson);
        schedule.get(lesson.getPeriod().getDay()).put(lesson.getPeriod().getHour(), temp);
    }

    public boolean isEmpty() {
        return schedule.isEmpty();
    }

    private String stringify(Map<Integer, List<Lesson>> day) {
        String output = "";
        for (int i = 1; i <= SCHEDULE_LENGTH; i++) {
            output += i + ": ";
            if (!(day.get(i).isEmpty())) {
                for (Lesson lesson : day.get(i)) {
                    output += lesson.getCourse().getSubject().getShortName() + "[" + lesson.getBlockLength() + "]" + flagify(lesson)
                            + ";";
                }
            } else {
                output += "N";
            }
            output += "##";
            while (output.contains("  ")) {
                output = output.replace("  ", " ");
            }
        }
        return output;

    }

    private String flagify(Lesson lesson) {
        String output = "{";
        int amount = 0;
        if (lesson.getUKWFlag()) {
            amount++;
            output += "(UKW)";
        }
        if (lesson.getGKWFlag()) {
            if (amount > 0) {
                output += ",";
                amount++;
            }
            output += "(GKW)";
        }
        if (!lesson.getGroupList().isEmpty()) {
            if (amount > 0) {
                output += ",";
                amount++;
            }
            output += lesson.getGroupList().toString();
        }
        if (lesson.isFWPM()) {
            if (amount > 0) {
                output += ",";
                amount++;
            }
            output += "(FWPM)";
        }
        if (lesson.getAltId() != 0) {
            if (amount > 0) {
                output += ",";
                amount++;
            }
            output += "(" + lesson.getAltId() + ")";
        }
        output += "}";
        return output;
    }

    private String stringifyHour(Map<Integer, List<Lesson>> day, int hour) {
        String output = "";
        for (Lesson lesson : day.get(hour)) {
            if (!day.get(hour).isEmpty()) {
                output += lesson.getCourse().getSubject().getShortName() + "[" + lesson.getBlockLength() + "]" + flagify(lesson)+ ";";
            } else {
                output += "N";
            }
            while (output.contains("  ")) {
                output = output.replace("  ", " ");
            }
        }
        return output;
    }

    public String parallelPrint() {
        int max = getMaxLength();
        String[] monday = stringify(schedule.get(MONDAY)).split("##");
        String[] tuesday = stringify(schedule.get(TUESDAY)).split("##");
        String[] wednesday = stringify(schedule.get(WEDNESDAY)).split("##");
        String[] thursday = stringify(schedule.get(THURSDAY)).split("##");
        String[] friday = stringify(schedule.get(FRIDAY)).split("##");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(getWhiteSpaced(monday[i], max))
                    .append(getWhiteSpaced(tuesday[i], max))
                    .append(getWhiteSpaced(wednesday[i], max))
                    .append(getWhiteSpaced(thursday[i], max))
                    .append(friday[i]);
            sb.append("\n");
        }
        return sb.toString();
    }

    private int getMaxLength() {
        int max = 0;
        String s;
        for (int i = 1; i <= 12; i++) {
            s = stringifyHour(schedule.get(MONDAY), i);
            if (s.length() > max) {
                max = s.length();
            }
            s = stringifyHour(schedule.get(TUESDAY), i);
            if (s.length() > max) {
                max = s.length();
            }
            s = stringifyHour(schedule.get(WEDNESDAY), i);
            if (s.length() > max) {
                max = s.length();
            }
            s = stringifyHour(schedule.get(THURSDAY), i);
            if (s.length() > max) {
                max = s.length();
            }
        }
        return max;
    }

    private String getWhiteSpaced(String string, int max) {
        int whites = Math.abs(string.length()-max-6);
        for (int i = 0; i < whites; i++) {
            string += " ";
        }
        return string;
    }

    public String toString() {
        String output = "\n" + "Monday: " + "\n" + stringify(schedule.get(MONDAY))
                + "\n" + "Tuesday: " + "\n" + stringify(schedule.get(TUESDAY))
                + "\n" + "Wednesday: " + "\n" + stringify(schedule.get(WEDNESDAY))
                + "\n" + "Thursday: " + "\n" + stringify(schedule.get(THURSDAY))
                + "\n" + "Friday: " + "\n" + stringify(schedule.get(FRIDAY));
        return output;
    }
}
