package hac.beans;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCoursesRepo extends JpaRepository <UserCourses, Long> {
}