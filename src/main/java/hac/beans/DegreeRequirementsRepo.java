package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DegreeRequirementsRepo extends JpaRepository<DegreeRequirement, Long> {
    boolean existsByRequirementName(String requirementName);

    @Query("SELECT SUM(d.mandatoryCredits) FROM DegreeRequirement d")
    Integer sumTotalMandatoryCredits();

    @Query("select d.requirementName from DegreeRequirement d")
    List<String> findAllRequirementNames();

}
