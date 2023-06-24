package hac.controllers;

import hac.beans.Course;
import hac.beans.CourseRepo;
import hac.beans.DegreeRequirements;
import hac.beans.DegreeRequirementsRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class CourseAdminController {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private DegreeRequirementsRepo degreeRequirementsRepo;

    @GetMapping("")
    public String adminIndex() {
        // TODO: for debugging only - remove this
        degreeRequirementsRepo.save(new DegreeRequirements( "קורס חובה מדמ\"ח", 105));
        degreeRequirementsRepo.save(new DegreeRequirements( "קורס בחירה מדמ\"ח", 5));
        degreeRequirementsRepo.save(new DegreeRequirements( "קורס בחירה כללי", 45));
        return "redirect:/admin/manageCourses";
    }

    @GetMapping("/manageCourses")
    public String main(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepo.findAll());
        return "admin/manage-courses";
    }

    @PostMapping("/addCourse")
    public String addCourse(@Valid Course course, BindingResult result, Model model) {
        System.out.println("addCourse: " + course);
        if (result.hasErrors()) {
            return "admin/manage-courses";
        }
        if(courseRepo.existsByCourseName(course.getCourseName())){
            model.addAttribute("message", "Course already exists.");
            model.addAttribute("course", new Course());
            model.addAttribute("courses", courseRepo.findAll());
            return "admin/manage-courses";
        }
        courseRepo.save(course);
        // pass the list of users to the view
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepo.findAll());
        return "admin/manage-courses";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") long id, Model model) {

        System.out.println("deleteUser: " + id); //TODO: remove this line
        // we throw an exception if the user is not found
        // since we don't catch the exception here, it will fall back on an error page (see below)
        Course user = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid user Id:" + id)
                );
        courseRepo.delete(user);
        model.addAttribute("users", courseRepo.findAll());
        return "redirect:/admin/manageCourses";
    }

    @GetMapping("/editCourse/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        System.out.println("showUpdateForm: " + id);
        Course course = courseRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        // the name "course"  is bound to the VIEW
        model.addAttribute("course", course);
        return "admin/edit-courses";
    }

    @GetMapping("/update/{id}")
    public String updateUserGet() {
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateCourse(@RequestParam("id") long id, @Valid Course course, BindingResult result, Model model) {
        System.out.println("updateUser: " + course);
        if (result.hasErrors()) {
            course.setId(id);
            return "admin/edit-courses";
        }
        course.setId(id);
        courseRepo.save(course);
        model.addAttribute("courses", courseRepo.findAll());

        return "admin/manage-courses";
    }

    @GetMapping("/requirements")
    public String showRequirementsPage(Model model) {
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        return "admin/requirements-page";
    }

    @PostMapping("/deleteRequirements")
    public String deleteRequirements(@RequestParam("id") long id, Model model) {

        System.out.println("deleteRequirements: " + id); //TODO: remove this line
        // we throw an exception if the user is not found
        // since we don't catch the exception here, it will fall back on an error page (see below)
        DegreeRequirements requirement = degreeRequirementsRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid DegreeRequirements Id:" + id)
                );
        degreeRequirementsRepo.delete(requirement);
        return "redirect:/admin/requirements";
    }

    @GetMapping("/editDegreeRequirement/{id}")
    public String showDegreeRequirementUpdateForm(@PathVariable("id") long id, Model model) {
        DegreeRequirements degreeRequirement = degreeRequirementsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid requirement Id:" + id));
        model.addAttribute("degreeRequirement", degreeRequirement);
        return "admin/edit-requirements";
    }

    @PostMapping("/updateDegreeRequirement")
    public String updateDegreeRequirement(@RequestParam("id") long id, @Valid DegreeRequirements requirement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            requirement.setId(id);
            return "admin/edit-requirements";
        }

        degreeRequirementsRepo.save(requirement);
        List<DegreeRequirements> requirements = degreeRequirementsRepo.findAll();
        return "redirect:/admin/requirements";
    }

//    @GetMapping("/delete/{id}")
//    public String deleteUserGet(@PathVariable long id) {
//
//        System.out.println("deleteUser: 222222");
//        return "redirect:/";
//    }
//
//    @GetMapping("/delete")
//    public String deleteUserGetNoParams() {
//        System.out.println("deleteUser: 333333");
//
//        return "redirect:/";
//    }

//
//    @GetMapping("/add-course")
//    public String showAddCoursePage(Model model) {
//        model.addAttribute("course", new Course());
//        return "add-course";
//    }

//    @GetMapping("/editCourse/{id}")
//    public String showUpdateForm(@PathVariable("id") long id, Model model) {
//        Course course = courseRepo.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
//        model.addAttribute("course", course);
//        return "edit-coureses";
//    }

//    @PostMapping("/updateCourse/{id}")
//    public String updateCourse(@PathVariable("id") long id, @Valid Course course,
//                               BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            course.setId(id);
//            return "edit-coureses";
//        }
//
//        courseRepo.save(course);
//        model.addAttribute("courses", courseRepo.findAll());
//        return "edit-coureses";
//    }

//    @GetMapping("/delete/{id}")
//    public String deleteUserGet(@PathVariable long id) {
//
//        System.out.println("deleteUser: 222222");
//        return "redirect:add-course";
//    }

//    @GetMapping("/delete")
//    public String deleteUserGetNoParams() {
//        System.out.println("deleteUser: 333333");
//
//        return "redirect:add-course";
//    }
}