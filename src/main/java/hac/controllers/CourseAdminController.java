package hac.controllers;

import hac.beans.Course;
import hac.beans.CourseRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseAdminController {

    @Autowired
    private CourseRepo courseRepo;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("course", new Course());
        return "add-course";
    }

    @PostMapping("/addCourse")
    public String addCourse(@Valid Course course, BindingResult result, Model model) {
        System.out.println("addCourse: " + course);
        if (result.hasErrors()) {
            return "add-course";
        }
        courseRepo.save(course);
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("course", new Course());
        return "add-course";
    }

    @GetMapping("/editCourse/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "edit-coureses";
    }

    @PostMapping("/updateCourse/{id}")
    public String updateCourse(@PathVariable("id") long id, @Valid Course course,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            course.setId(id);
            return "edit-coureses";
        }

        courseRepo.save(course);
        model.addAttribute("courses", courseRepo.findAll());
        return "edit-coureses";
    }

    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("id") long id, Model model) {

        System.out.println("deleteUser: 1111111");
        // we throw an exception if the user is not found
        // since we don't catch the exception here, it will fall back on an error page (see below)
        Course user = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid course Id:" + id)
                );
        courseRepo.delete(user);
        model.addAttribute("courses", courseRepo.findAll());
        return "add-course";
    }

//    @GetMapping("/delete/{id}")
//    public String deleteUserGet(@PathVariable long id) {
//
//        System.out.println("deleteUser: 222222");
//        return "redirect:add-course";
//    }
//
//    @GetMapping("/delete")
//    public String deleteUserGetNoParams() {
//        System.out.println("deleteUser: 333333");
//
//        return "redirect:add-course";
//    }
}
