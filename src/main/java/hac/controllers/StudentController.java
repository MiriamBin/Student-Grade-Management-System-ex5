package hac.controllers;

import hac.beans.Course;
import hac.beans.CourseRepo;
import hac.beans.UserCourses;
import hac.beans.UserCoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@RequestMapping("/user")  TODO: uncomment this line
@Controller
public class StudentController {

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @Autowired
    private CourseRepo courseRepo;

    @GetMapping("/userCatalog")
    public String main(Model model) {
        System.out.println("userCatalog:!!!!!!!!!!!!!!!!!!! " );
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());
        return "user/course-catalog";
    }

    @PostMapping("/addToStudentList")
    public String addCourseToStudentList(@RequestParam("id") long id, Model model) {
        System.out.println("addToStudentList: " + id);

        Course newCourse = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid user Id:" + id)
                );

        UserCourses existingUserCourses = userCoursesRepo.findByCourseAndUsername(newCourse, "Shlomo");
        if (existingUserCourses == null) {
            userCoursesRepo.save( new UserCourses(newCourse, "Shlomo",90) );
            System.out.println("courseRepo: " + userCoursesRepo.findAll());
            model.addAttribute("message", "Course added successfully.");
        } else {
            model.addAttribute("message", "Course already exists for this user.");
        }

        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());

        return "user/course-catalog";
    }

    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("id") long id, Model model) {
        UserCourses userCourse = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        userCoursesRepo.delete(userCourse);
        System.out.println("deleteCourse1111111: " + id);
        model.addAttribute("message", "Course deleted successfully.");
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());

        return "/user/course-catalog";
    }

    @PostMapping("/editCourseGrade")
    public String editCourseGrade(@RequestParam("id") long id, @RequestParam("grade") Integer grade, Model model) {
        UserCourses userCourse = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        userCourse.setGrade(grade);
        userCoursesRepo.save(userCourse);
        System.out.println("editCourseGrade2222222222222");

        model.addAttribute("message", "Course grade edited successfully.");
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());

        return "/user/course-catalog";
    }

    @GetMapping("/myCourses")
    public String myCourses(Model model) {
        System.out.println("myCourses6666666666666666");
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());
        return "/user/my-courses";
    }
}