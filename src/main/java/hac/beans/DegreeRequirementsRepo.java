package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DegreeRequirementsRepo extends JpaRepository<DegreeRequirement, Long> {
    /**
     * check if requirement exists by requirement name
     * @param requirementName requirement name
     * @return true if requirement exists, false otherwise
     */
    boolean existsByRequirementName(String requirementName);

    /**
     * find requirement by requirement name
     * @param requirementName requirement name
     * @return  requirement
     */

    DegreeRequirement findByRequirementName(String requirementName);

    /**
     * sum all mandatory credits
     * @return sum of all mandatory credits
     */
    @Query("SELECT SUM(d.mandatoryCredits) FROM DegreeRequirement d")
    Integer sumTotalMandatoryCredits();

    /**
     * find all requirement names
     * @return list of all requirement names
     */
    @Query("select d.requirementName from DegreeRequirement d")
    List<String> findAllRequirementNames();
}
