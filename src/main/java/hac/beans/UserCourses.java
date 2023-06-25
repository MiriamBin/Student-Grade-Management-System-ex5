package hac.beans;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    private String username;

    @Min(message = "יש להזין ציון בין 0 ל-100", value = 0)
    @Max(message = "יש להזין ציון בין 0 ל-100", value = 100)
    private Integer grade;

    public UserCourses() {}

    public UserCourses(Course course, String username, Integer grade) {
        this.course = course;
        this.username = username;
        this.grade = grade;
    }
}