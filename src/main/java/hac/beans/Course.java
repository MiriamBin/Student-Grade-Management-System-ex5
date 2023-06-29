package hac.beans;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * JPA Entity Class for Course
 * this class represents a course in the system
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "יש להזין שם קורס")
    @NotBlank(message = "יש להזין אותיות")
    private String courseName;

    @NotNull(message = "שדה חובה")
    @Min(message = "יש להזין ערך גדול מ-0", value = 1)
    private Integer credit;

    @NotEmpty(message = "יש להזין סוג קורס")
    @NotBlank(message = "יש להזין אותיות")
    private String requirementType;
}
