package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DegreeRequirementsRepo extends JpaRepository<DegreeRequirements, Long> {
    boolean existsByRequirementName(String requirementName);

    @Query("SELECT SUM(d.mandatoryCredits) FROM DegreeRequirements d")
    Integer sumTotalMandatoryCredits();


//    @Query("SELECT SUM(d.mandatoryCredits) FROM DegreeRequirements d WHERE d.requirementName LIKE '%מדמ\"ח%'")
//    Integer sumTotalMandatoryCreditsInComputerScience();

}
