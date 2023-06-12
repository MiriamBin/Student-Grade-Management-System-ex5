package hac.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotEmpty(message = "Name is mandatory")
    private String courseName;

    @Positive(message = "Course credit must be positive")
    private int credit;

    public Course() {}

    public Course(String courseName, int credit) {
        this.courseName = courseName;
        this.credit = credit;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
    public int getCredit() {
        return credit;
    }
}
