package domain;

public class Room {

    private String number;
    private String type;
    private int capacity;

    public Room () {

    }

    public Room(int flag) {
        if(flag == 1) {
            this.number = "NO_ROOM";
            this.type = "NO_ROOM";
            this.capacity = 0;
        }
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String toString() {
        return number + " " + type + " " + capacity;
    }
}
