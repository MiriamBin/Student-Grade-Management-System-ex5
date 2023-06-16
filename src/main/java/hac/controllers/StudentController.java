package hac.controllers;

import hac.beans.Course;
import hac.beans.CourseRepo;
import hac.beans.UserCourses;
import hac.beans.UserCoursesRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @Autowired
    private CourseRepo courseRepo;

    @GetMapping("/userCatalog")
    public String main(Model model) {

        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());
        return "course-catalog";
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

        return "course-catalog";
    }

}
