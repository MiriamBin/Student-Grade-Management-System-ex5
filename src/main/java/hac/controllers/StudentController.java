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

import java.security.Principal;

@RequestMapping("/user")
@Controller
public class StudentController {

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @Autowired
    private CourseRepo courseRepo;

    @GetMapping("")
    public String adminIndex() {
        return "redirect:/user/userCatalog";
    }

    @GetMapping("/userCatalog")
    public String userCatalog(Model model, Principal principal) {
        System.out.println("userCatalog:!!!!!!!!!!!!!!!!!!! " ); //TODO: TO DELETE
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findByUsername(principal.getName()));

        return "user/course-catalog";
    }

    @PostMapping("/addToStudentList")
    public String addCourseToStudentList(@RequestParam("id") long id, Model model, Principal principal) {
        System.out.println("addToStudentList: " + id);

        Course newCourse = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid user Id:" + id)
                );

        UserCourses existingUserCourses = userCoursesRepo.findByCourseAndUsername(newCourse, "Shlomo"); //TODO: change the username to the correct value
        if (existingUserCourses == null) {
            userCoursesRepo.save( new UserCourses(newCourse,principal.getName(),90) ); //TODO: change the grade to the correct value
            System.out.println("courseRepo: " + userCoursesRepo.findAll());
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
        System.out.println("deleteCourse1111111: " + id); //TODO: TO DELETE
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
        System.out.println("editCourseGrade2222222222222"); //TODO: TO DELETE

        model.addAttribute("message", "Course grade edited successfully."); // TOdo: change the message
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());

        return "/user/course-catalog";
    }

    @GetMapping("/myCourses")
    public String myCourses(Model model, Principal principal) {
        System.out.println("myCourses6666666666666666"); //TODO: TO DELETE
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findByUsername(principal.getName()));

        return "/user/my-courses";
    }
}