package domain;

import util.helpers.own.container.PreferenceType;

public class Preference {

    private PreferenceType type;
    private int day;
    private int hour;
    private int constraint;
    private Lecturer lecturer;

    public PreferenceType getType() {
        return type;
    }

    public void setType(PreferenceType type) {
        this.type = type;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getConstraint() {
        return constraint;
    }

    public void setConstraint(int constraint) {
        this.constraint = constraint;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public String toString() {
        if(!this.type.equals(PreferenceType.L)) {
            return this.lecturer.getShortName() + " " + this.getDay() + " " + this.getHour() + " " + this.constraint;
        }
        return this.type.toString() + " " + this.getDay() + " " + this.getHour() + " " + this.constraint;
    }
}
