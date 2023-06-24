package hac.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class DegreeRequirements implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    @NotNull(message = "שדה חובה")
//    @NotEmpty(message = "יש להזין שם")
//    @NotBlank(message = "יש להזין שם")

    @NotNull
    @NotEmpty(message = "יש להוסיף שם")
    private String requirementName;

//    @NotNull(message = "שדה חובה")
//    @Min(message = "יש להזין ערך גדול מ-0", value = 1)
//    @Positive(message = "יש להזין ערך חיובי")
    @Positive(message = "Course credit must be positive")
    private Integer mandatoryCredits;

    public DegreeRequirements(String requirementName, Integer mandatoryCredits) {
        this.requirementName = requirementName;
        this.mandatoryCredits = mandatoryCredits;
    }
}
