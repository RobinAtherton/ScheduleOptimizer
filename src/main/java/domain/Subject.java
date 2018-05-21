package domain;

public class Subject {

    private String shortName;
    private String longName;
    private String baseRoom;

    public Subject() {

    }

    public Subject(int flag) {
        if(flag == 1) {
            this.shortName = "NO_SUBJECT";
        }
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

    public String getBaseRoom() {
        return baseRoom;
    }

    public void setBaseRoom(String baseRoom) {
        this.baseRoom = baseRoom;
    }
}
