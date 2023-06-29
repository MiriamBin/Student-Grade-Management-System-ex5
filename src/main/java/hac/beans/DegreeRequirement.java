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

/**
 * JPA Entity Class for DegreeRequirement (DegreeRequirement is a requirement for a degree)
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class DegreeRequirement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "שדה חובה")
    @NotBlank(message = "יש להזין אותיות")
    private String requirementName;

    @NotNull(message = "שדה חובה")
    @Min(message = "יש להזין ערך גדול מ-0", value = 1)
    private Integer mandatoryCredits;

    public DegreeRequirement(String requirementName, Integer mandatoryCredits) {
        this.requirementName = requirementName;
        this.mandatoryCredits = mandatoryCredits;
    }
}
