package data.output;

import app.ScheduleSolution;
import domain.Lesson;
import domain.Semester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleChangeLogger {

    private final ScheduleSolution before;
    private final String[] changes;

    public ScheduleChangeLogger(ScheduleSolution before, ScheduleSolution after) {
        this.before = before;
        this.changes = getDifferences(before, after);
    }

    public void log() {
        Map<String, List<String>> map = createSortedMap(before);
        for (String outer : map.keySet()) {
            System.out.println(outer + ":");
            if (!map.get(outer).isEmpty()) {
                for (String inner : map.get(outer)) {
                    System.out.println("\t" + inner);
                }
            } else {
                System.out.println("\t" + "no changes.");
            }
        }
    }

    private String[] getDifferences(ScheduleSolution before, ScheduleSolution after) {
        StringBuilder sb = new StringBuilder();
        List<Lesson> passed = new ArrayList<>();
        for (Lesson outer : before.getLessons()) {
            for (Lesson inner : after.getLessons()) {
                if (outer.getCourse().toHash() == inner.getCourse().toHash()
                        && outer.getHour() != inner.getHour()
                        && !passed.contains(outer) && !passed.contains(inner)) {
                    sb.append(outer.toString()).append("[").append(outer.getPeriod().toString()).append("]").append("{" + affix(outer) + "}");
                    sb.append(" ----> ");
                    sb.append(inner.getPeriod().toString());
                    sb.append("\n");
                    sb.append("#x#");
                    passed.add(outer);
                    passed.add(inner);
                }
            }
        }
        return sb.toString().split("#x#");
    }

    private String affix(Lesson lesson) {
        String affixes = "";
        boolean first = true;
        if (lesson.getUKWFlag() || lesson.getAltId() == 1) {
            if (first) {
                affixes += "(UKW)";
                first = false;
            } else {
                affixes += ",";
                affixes += "(UKW)";
            }
        }
        if (lesson.getGKWFlag() || lesson.getAltId() == 2) {
            if (first) {
                affixes += "(GKW)";
                first = false;
            } else {
                affixes += ",";
                affixes += "(GKW)";
            }
        }
        if (!lesson.getGroupList().isEmpty()) {
            int i = 0;
            if (first) {
                affixes = assertGroupAffix(lesson, affixes, i);
                first = false;
            }
            else {
                affixes += ",";
                affixes = assertGroupAffix(lesson, affixes, i);
            }
        }
        return affixes;
    }

    private String assertGroupAffix(Lesson lesson, String affixes, int i) {
        for (String group : lesson.getGroupList()) {
            affixes+=group;
            if (lesson.getGroupList().size() - i > 1) {
                affixes+=",";
            }
            i++;
        }
        return affixes;
    }

    private Map<String, List<String>> createSortedMap(ScheduleSolution before) {
        Map<String, List<String>> sortedMap = new HashMap<>();
        for (Semester semester : before.getSemesters()) {
            sortedMap.put(semester.getShortName(), new ArrayList<String>());
        }
        for (String outer : changes) {
            for (String inner : sortedMap.keySet()) {
                if (outer.contains(inner)) {
                    sortedMap.get(inner).add(outer);
                }
            }
        }
        return sortedMap;
    }
}
