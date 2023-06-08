package hac.controllers;

import hac.beans.Course;
import hac.beans.CourseRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

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
        if (result.hasErrors()) {
            return "add-course";
        }
        courseRepo.save(course);
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("course", new Course());
        return "add-course";
    }

}
