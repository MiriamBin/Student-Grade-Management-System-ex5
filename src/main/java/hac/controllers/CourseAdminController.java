package hac.controllers;

import hac.beans.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CourseAdminController {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @Autowired
    private DegreeRequirementsRepo degreeRequirementsRepo;

    @GetMapping("")
    public String adminIndex() {
        // TODO: for debugging only - remove this
        degreeRequirementsRepo.save(new DegreeRequirement( "קורס חובה מדמ\"ח", 105));
        degreeRequirementsRepo.save(new DegreeRequirement( "קורס בחירה מדמ\"ח", 5));
        degreeRequirementsRepo.save(new DegreeRequirement( "קורס בחירה כללי", 45));
        return "redirect:/admin/manageCourses";
    }

    @GetMapping("/manageCourses")
    public String manageCourses(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/manage-courses";
    }
    @PostMapping("/delete")
    public String deleteCourse(@RequestParam("id") long id, Model model) {

        System.out.println("deleteCourse: " + id); //TODO: remove this line
        // we throw an exception if the user is not found
        // since we don't catch the exception here, it will fall back on an error page (see below)
        Course course = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid course Id:" + id)
                );

        List<UserCourses> userCourses = userCoursesRepo.findByCourseId(id);
        if(!userCourses.isEmpty()) {
            model.addAttribute("message", "לא ניתן למחוק קורס שהוקצה לסטודנטים");
            model.addAttribute("course", new Course());
            model.addAttribute("courses", courseRepo.findAll());

            return "admin/manage-courses";
        } else {
            courseRepo.delete(course);
        }

        model.addAttribute("courses", courseRepo.findAll());
        return "redirect:/admin/manageCourses";
    }

//    @PostMapping("/delete")
//    public String deleteUser(@RequestParam("id") long id, Model model) {
//
//        System.out.println("deleteUser: " + id); //TODO: remove this line
//        // we throw an exception if the user is not found
//        // since we don't catch the exception here, it will fall back on an error page (see below)
//        Course user = courseRepo
//                .findById(id)
//                .orElseThrow(
//                        () -> new IllegalArgumentException("Invalid user Id:" + id)
//                );
//        courseRepo.delete(user);
//        model.addAttribute("users", courseRepo.findAll());
//        return "redirect:/admin/manageCourses";
//    }

    @GetMapping("/editCourse/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        System.out.println("showUpdateForm: " + id); //TODO: remove this line
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
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        return "admin/requirements-page";
    }

    @PostMapping("/deleteRequirements")
    public String deleteRequirements(@RequestParam("id") long id, Model model) {
        DegreeRequirement requirement = degreeRequirementsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid DegreeRequirements Id:" + id));
        degreeRequirementsRepo.delete(requirement);
        return "redirect:/admin/requirements";
    }

    @GetMapping("/editDegreeRequirement/{id}")
    public String showDegreeRequirementUpdateForm(@PathVariable("id") long id, Model model) {
        DegreeRequirement degreeRequirement = degreeRequirementsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid requirement Id:" + id));
        model.addAttribute("degreeRequirement", degreeRequirement);
        return "admin/edit-requirements";
    }

    @PostMapping("/updateDegreeRequirement")
    public String updateDegreeRequirement(@RequestParam("id") long id, @Valid DegreeRequirement requirement, BindingResult result) {
        if (result.hasErrors()) {
            requirement.setId(id);
            return "admin/edit-requirements";
        }
        degreeRequirementsRepo.save(requirement);
        return "redirect:/admin/requirements";
    }

@GetMapping("/newDegreeRequirement")
public String newDegreeRequirement(Model model) {
    model.addAttribute("degreeRequirement", new DegreeRequirement());
    model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
    model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
    return "admin/requirements-page";
}

    @PostMapping("/addDegreeRequirement")
    public String addDegreeRequirement(@Valid DegreeRequirement degreeRequirement, BindingResult result, Model model) {


        System.out.println("addDegreeRequirement: " + degreeRequirement);
        if (result.hasErrors()) {
            model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
            model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
            return "admin/requirements-page";
        }
        String requirementDegreeName = degreeRequirement.getRequirementName();
        if(degreeRequirementsRepo.existsByRequirementName(degreeRequirement.getRequirementName())){
            String existMessage = String.format("סוג הקורסים  \"%s\" כבר קיים.", requirementDegreeName);
            model.addAttribute("existMessage", existMessage);
            model.addAttribute("degreeRequirement", new DegreeRequirement());
            model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
            model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        }
        else {
            String addedMessage = String.format("סוג הקורסים \"%s\" נוספה בהצלחה.", requirementDegreeName);
            degreeRequirementsRepo.save(degreeRequirement);
            model.addAttribute("addedMessage", addedMessage);
            model.addAttribute("degreeRequirement", new DegreeRequirement());
            model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
            model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        }

        // pass the list of users to the view
        model.addAttribute("degreeRequirement", new DegreeRequirement());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());

        return "admin/requirements-page";
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
            model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
            return "admin/manage-courses";
        }
        courseRepo.save(course);
        // pass the list of users to the view
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/manage-courses";
    }

    private DegreeRequirement getTotalDegreeRequirement(){
        String requirementName = "סך הכל נדרש";
        Integer totalCredits = degreeRequirementsRepo.sumTotalMandatoryCredits();
        return new DegreeRequirement(requirementName, totalCredits == null ? 0 : totalCredits);
    }

    private boolean isDegreeRequirementExists(String requirementName){
        return degreeRequirementsRepo.existsByRequirementName(requirementName);
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