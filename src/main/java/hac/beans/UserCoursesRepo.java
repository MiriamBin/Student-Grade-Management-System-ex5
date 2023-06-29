package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCoursesRepo extends JpaRepository <UserCourses, Long> {
    UserCourses findByCourseAndUsername(Course course, String username);
    List<UserCourses> findByUsername(String username);
    List<UserCourses> findByCourseId(Long courseId);

    @Query("SELECT u FROM UserCourses u WHERE u.username = :username AND u.course.requirementType = :requirementType")
    List<UserCourses> findByUsernameAndRequirementType(String username, String requirementType);
}