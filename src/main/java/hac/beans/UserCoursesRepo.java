package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCoursesRepo extends JpaRepository <UserCourses, Long> {
    UserCourses findByCourseAndUsername(Course course, String username);
    List<UserCourses> findByUsername(String username);
}