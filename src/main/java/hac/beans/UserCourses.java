package hac.beans;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

@Entity

public class UserCourses implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Course course;

    @NotEmpty(message = "Username is mandatory")
    private String username;

    private Integer grade;

    public UserCourses() {}

    public UserCourses(Course course, String username, Integer grade) {
        this.course = course;
        this.username = username;
        this.grade = grade;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
    public Course getCourse() {
        return course;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public Integer getGrade() {
        return grade;
    }
}
