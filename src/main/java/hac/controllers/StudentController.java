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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class StudentController {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @GetMapping("")
    public String userHomePage() {
        return "redirect:/user/userCatalog";
    }

    @GetMapping("/userCatalog")
    public String userCatalog(Model model, Principal principal) {
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findByUsername(principal.getName()));
        return "user/course-catalog";
    }

    @PostMapping("/addToStudentList")
    public String addCourseToStudentList(@RequestParam("id") long id, Model model, Principal principal) {
        Course newCourse = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid user Id:" + id)
                );
        UserCourses existingUserCourses = userCoursesRepo.findByCourseAndUsername(newCourse, principal.getName());

        if (existingUserCourses == null) {
            userCoursesRepo.save( new UserCourses(newCourse,principal.getName(),null) );
            model.addAttribute("message", "Course added successfully.");

        } else {
            model.addAttribute("message", "Course already exists for this user.");
        }
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findByUsername(principal.getName()));
        return "user/course-catalog";
    }

    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("id") long id, Model model) {
        UserCourses userCourse = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        userCoursesRepo.delete(userCourse);
        model.addAttribute("message", "Course deleted successfully.");
        return "redirect:/user/myCourses";
    }

    @PostMapping("/editCourse")
    public String editCourse(@RequestParam("id") long id, Model model) {
        UserCourses userCourses = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("userCourses",userCourses);
        return "user/edit-user-course";
    }

    @PostMapping("/updateCourse")
    public String updateCourse(@RequestParam("id") long id, @Valid UserCourses userCourses, BindingResult result) {
        System.out.println("~~~~~~~~~~~~~~~~~ updateCourse() ~~~~~~~~~~~~~~~~~");

        if (result.hasErrors()) {
            return "user/edit-user-course";
        }

        // Retrieve the course from the database based on the given id
        UserCourses existingUserCourses = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        // Update the grade value
        existingUserCourses.setGrade(userCourses.getGrade());

        // Save the updated course in the database
        userCoursesRepo.save(existingUserCourses);

        return "redirect:/user/myCourses";
    }


    @GetMapping("/myCourses")
    public String myCourses(Model model, Principal principal) {
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findByUsername(principal.getName()));
        return "/user/my-courses";
    }
}