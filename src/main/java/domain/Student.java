package domain;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private int mtrNumber;
    private String semester;
    private List<String> attendedFWPM = new ArrayList<>();

    public Student() {

    }
    public void addFWPM(String FWPM) {
        this.attendedFWPM.add(FWPM);
    }

    public int getMtrNumber() {
        return mtrNumber;
    }

    public void setMtrNumber(int mtrNumber) {
        this.mtrNumber = mtrNumber;
    }

    public List<String> getAttendedFWPM() {
        return attendedFWPM;
    }

    public void setAttendedFWPM(List<String> attendedFWPM) {
        this.attendedFWPM = attendedFWPM;
    }

    public String getSemesterName() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String toString() {
        String FWPMS = "";
        for (String s : this.attendedFWPM) {
            FWPMS +=s;
            FWPMS += " ";
        }
        return "Matrikelnummer: " + this.mtrNumber + " FWPMS: " +  FWPMS;
    }
}
