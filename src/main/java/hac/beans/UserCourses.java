package hac.beans;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
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
}