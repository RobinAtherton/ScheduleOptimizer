package domain;

public class Course {

    private int id;
    private Lecturer lecturer;
    private Subject subject;
    private Semester semester;

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return lecturer.getShortName() + " " + subject.getShortName() + " " + semester.getShortName();
    }

    public int toHash() {
        return (lecturer.getShortName() + subject.getShortName() + semester.getShortName()).hashCode();
    }
}
