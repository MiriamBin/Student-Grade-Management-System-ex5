package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {

    boolean existsByCourseName(String courseName);

    List<Course> findByRequirementType(String requirementType);

}