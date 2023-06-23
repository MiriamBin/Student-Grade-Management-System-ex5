package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, Long> {

    boolean existsByCourseName(String courseName);
}