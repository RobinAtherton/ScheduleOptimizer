package domain;

public class Period {

    private int id;
    private int day;
    private int hour;

    public Period() {
        this.day = 1;
        this.hour = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String toString() {
        return "Day:" + this.getDay() + " Hour:" + this.getHour();
    }
}
