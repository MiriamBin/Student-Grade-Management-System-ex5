package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCoursesRepo extends JpaRepository <UserCourses, Long> {
    /**
     * find user course by course and username
     * @param course course
     * @param username username
     * @return user course if exists, null otherwise
     */
    UserCourses findByCourseAndUsername(Course course, String username);

    /**
     * find user course by course and username
     * @param username username
     * @return user course
     */
    List<UserCourses> findByUsername(String username);

    /**
     * find user course by course id
     * @param courseId course id
     * @return user course
     */
    List<UserCourses> findByCourseId(Long courseId);

    /**
     * find user course by course id and username
     * @param username username
     * @param requirementType requirement type
     * @return user course
     */
    @Query("SELECT u FROM UserCourses u WHERE u.username = :username AND u.course.requirementType = :requirementType")
    List<UserCourses> findByUsernameAndRequirementType(String username, String requirementType);
}