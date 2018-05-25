package domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;
import util.ruleLogic.CollisionDetector;
import util.helpers.optaplanner.listeners.PeriodShiftingVariableListener;

import java.util.ArrayList;
import java.util.List;


@PlanningEntity()
public class Lesson {

    private int id;
    private Course course;
    private Period period;
    private Room room;
    private int blockLength = 1;

    private boolean prime = false;
    private boolean uKW = false;
    private boolean gKW = false;
    private boolean FWPM = false;
    private boolean practical = false;

    private List<Student> fwpmStudents = new ArrayList<>();
    private int altId = 0;
    private String collisionReason;

    private List<String> groupList = new ArrayList<>();
    private List<Lesson> sameSemester = new ArrayList<>();
    private List<Lesson> coupledLessons = new ArrayList<>();
    private List<Lesson> dodgeableLessons = new ArrayList<>();

    //shadow variables
    private List<Lesson> sameDay = new ArrayList<>();
    private List<Lesson> fwpmBlocked = new ArrayList<>();
    private List<Lesson> practicalBlocked = new ArrayList<>();

    public Lesson() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @PlanningVariable(valueRangeProviderRefs = "periodId")
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    //@PlanningVariable(valueRangeProviderRefs = "roomId") //IDK if nullable is accepted
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isPrime() {
        return prime;
    }

    public void setPrime(boolean prime) {
        this.prime = prime;
    }

    public boolean isPractical() {
        return practical;
    }

    public void setPractical(boolean practical) {
        this.practical = practical;
    }

    public int getBlockLength() {
        return blockLength;
    }

    public void setBlockLength(int blockLength) {
        this.blockLength = blockLength;
    }

    public boolean getUKWFlag() {
        return uKW;
    }

    public void setUKWFlag(boolean straight) {
        this.uKW = straight;
    }

    public boolean getGKWFlag() {
        return gKW;
    }

    public void setGKWFlag(boolean gKW) {
        this.gKW = gKW;
    }

    public int getAltId() {
        return altId;
    }

    public void setAltId(int altId) {
        this.altId = altId;
    }

    public boolean isFWPM() {
        return FWPM;
    }

    public void setFWPM(boolean FWPM) {
        this.FWPM = FWPM;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

    public void addFWPMStudent(Student student) {
        this.fwpmStudents.add(student);
    }

    public List<Student> getFWPMStudents() {
        return this.fwpmStudents;
    }

    public String getCollisionReason() {
        return collisionReason;
    }

    public void setCollisionReason(String collisionReason) {
        this.collisionReason = collisionReason;
    }

    public boolean extensiveCollides(Lesson lesson) {
        return CollisionDetector.extensiveCollision(this, lesson);
    }

    public boolean softCollides(Lesson lesson) {
        return CollisionDetector.softFWPMCollision(this, lesson);
    }

    public boolean prefCollides(Preference preference) {
        return CollisionDetector.preferenceCollision(this, preference);
    }

    public boolean coupledCollides(Lesson lesson) {
        return CollisionDetector.coupledCollision(this, lesson);
    }

    public int checkForCompactness() {
        return CollisionDetector.checkCompactness(this);
    }

    public String toString() {
        String groups = "";
        int size = groupList.size();
        for (int i = 0; i < size; i++) {
            if (i == size-1 || size == 1) {
                groups += groupList.get(i);
            } else {
                groups += groupList.get(i) + ",";
            }
        }
        return +this.getId()
                + " " + this.getCourse().getSemester().getShortName()
                + " " + this.getCourse().getLecturer().getShortName()
                + " " + this.getCourse().getSubject().getShortName()
                + " " + this.getRoom().getNumber() + " " + groups;
    }

    @CustomShadowVariable(variableListenerClass = PeriodShiftingVariableListener.class,
            sources = {@PlanningVariableReference(variableName = "period")})
    public List<Lesson> getSameDay() {
        return this.sameDay;
    }

    public void addSameDay(Lesson lesson) {
        this.sameDay.add(lesson);
    }

    public void setSameDay(List<Lesson> sameDay) {
        this.sameDay = sameDay;
    }

    @CustomShadowVariable(variableListenerRef = @PlanningVariableReference(variableName = "sameDay"))
    public List<Lesson> getFwpmBlocked() {
        return fwpmBlocked;
    }

    public void setFwpmBlocked(List<Lesson> fwpmBlocked) {
        this.fwpmBlocked = fwpmBlocked;
    }

    @CustomShadowVariable(variableListenerRef = @PlanningVariableReference(variableName = "sameDay"))
    public List<Lesson> getPracticalBlocked() {
        return practicalBlocked;
    }

    public void setPracticalBlocked(List<Lesson> practicalBlocked) {
        this.practicalBlocked = practicalBlocked;
    }

    public List<Lesson> getSameSemester() {
        return sameSemester;
    }

    public void setSameSemester(List<Lesson> sameSemester) {
        this.sameSemester = sameSemester;
    }

    public List<Lesson> getCoupledLessons() {
        return coupledLessons;
    }

    public void setCoupledLessons(List<Lesson> coupledLessons) {
        this.coupledLessons = coupledLessons;
    }

    public List<Lesson> getDodgeableLessons() {
        return dodgeableLessons;
    }

    public void setDodgeableLessons(List<Lesson> dodgeableLessons) {
        this.dodgeableLessons = dodgeableLessons;
    }

    public void print() {
        System.out.println(toString());
    }

    public String getSemesterName() {
        return this.getCourse().getSemester().getShortName();
    }

    public String getLecturerName() {
        return this.getCourse().getLecturer().getShortName();
    }

    public String getSubjectName() {
        return this.getCourse().getSubject().getShortName();
    }

    public String getRoomName() {
        return this.getRoom().getNumber();
    }

    public int getDay() {
        return this.getPeriod().getDay();
    }

    public int getHour() {
        return this.getPeriod().getHour();
    }
}
