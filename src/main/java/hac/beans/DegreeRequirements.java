package hac.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
public class DegreeRequirements implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private long id;

    @NotNull(message = "mandatory field")
    private Integer electiveCredits;

    @NotNull(message = "mandatory field")
    private Integer mandatoryCredits;

    @NotNull(message = "mandatory field")
    private Integer generalCredits;

    public DegreeRequirements() {
        this.electiveCredits = 0;
        this.mandatoryCredits = 0;
        this.generalCredits = 0;
    }
}
