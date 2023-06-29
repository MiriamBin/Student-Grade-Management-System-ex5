package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepo extends JpaRepository<Course, Long> {
    /**
     * check if course exists by course name
     * @param courseName course name
     * @return true if course exists, false otherwise
     */
    boolean existsByCourseName(String courseName);

    /**
     * find course by course name
     * @param courseName course name
     * @return course
     */
    Course findByCourseName(String courseName);
}