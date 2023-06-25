package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCoursesRepo extends JpaRepository <UserCourses, Long> {
    UserCourses findByCourseAndUsername(Course course, String username);
    List<UserCourses> findByUsername(String username);
    List<UserCourses> findByCourseId(Long courseId);

//    @Query("SELECT COUNT(u) FROM UserCourses u WHERE u.username = :username AND u.grade IS NOT NULL")
//    Long countCoursesWithGradeForUser(@Param("username") String username);
//
//    Double calculateGPAForUser(String name);
}