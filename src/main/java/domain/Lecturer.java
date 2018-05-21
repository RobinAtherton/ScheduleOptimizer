package domain;

import java.util.ArrayList;
import java.util.List;

public class Lecturer {

    private String shortName;   //0
    private String longName;    //1
    private String title;       //23
    private List<Preference> preferences = new ArrayList<>();


    public Lecturer() {

    }
    public Lecturer(int flag) {
        shortName = "NO_LECTURER";
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public void addPreference(Preference preference) {
        this.preferences.add(preference);
    }
}
