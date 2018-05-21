package domain;

public class Semester {

    private String shortName;
    private String longName;
    private int capacity;

    public Semester() {

    }

    public Semester(int flag) {
        if (flag == 1)
            shortName = "NO_SEMESTER";
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
