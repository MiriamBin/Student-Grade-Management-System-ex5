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
public class AdminController {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @Autowired
    private DegreeRequirementsRepo degreeRequirementsRepo;

    @GetMapping("")
    public String adminIndex() {
        return "admin/home-page";
    }

    @GetMapping("/manageCourses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/manage-courses";
    }

    @PostMapping("/addCourse")
    public synchronized String addCourse(@Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("courses", courseRepo.findAll());
            model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
            return "admin/manage-courses";
        }
        String courseName = course.getCourseName();
        if(courseRepo.existsByCourseName(courseName)){
            String existMessage = String.format("שם הקורס \"%s\" כבר קיים. הזן שם אחר.", courseName);
            model.addAttribute("existMessage", existMessage);
            model.addAttribute("course", new Course());
            model.addAttribute("courses", courseRepo.findAll());
            model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
            return "admin/manage-courses";
        }
        courseRepo.save(course);
        String addedMessage = String.format("הקורס \"%s\" נוסף בהצלחה.", courseName);
        model.addAttribute("addedMessage", addedMessage);
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/manage-courses";
    }

    @PostMapping("/delete")
    public synchronized String deleteCourse(@RequestParam("id") long id, Model model) {
        try {
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
        } catch (org.springframework.dao.DataAccessException | org.hibernate.exception.JDBCConnectionException e) {
            model.addAttribute("error", "הייתה בעיה במסד הנתונים. נסה שנית מאוחר יותר.");
            return "error";
        }

        model.addAttribute("courses", courseRepo.findAll());
        return "redirect:/admin/manageCourses";
    }

    @GetMapping("/editCourse/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Course course = courseRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/edit-courses";
    }

    @PostMapping("/update")
    public synchronized  String updateCourse(@RequestParam("id") long id, @Valid Course course, BindingResult result, Model model) {

        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        if (result.hasErrors()) {
            course.setId(id);
            return "admin/edit-courses";
        }

        String courseName = course.getCourseName();
        Course courseFromDB = courseRepo.findByCourseName(courseName);
        if(courseFromDB != null && courseFromDB.getId() != id){
            String existMessage = String.format("שם הקורס \"%s\" כבר קיים. הזן שם אחר.", courseName);
            model.addAttribute("message", existMessage);
            return "admin/edit-courses";
        }

        course.setId(id);
        courseRepo.save(course);
        return "redirect:/admin/manageCourses";
    }

    @GetMapping("/newCourse")
    public String newCourse(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/manage-courses";
    }

    @GetMapping("/requirements")
    public String showRequirementsPage(Model model) {
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        return "admin/requirements-page";
    }

    @PostMapping("/deleteRequirements")
    public synchronized  String deleteRequirements(@RequestParam("id") long id, Model model) {
        try {
            DegreeRequirement requirement = degreeRequirementsRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid DegreeRequirements Id:" + id));
            degreeRequirementsRepo.delete(requirement);
            return "redirect:/admin/requirements";
        } catch (Exception e) {
            model.addAttribute("error", "הייתה בעיה במסד הנתונים. נסה שנית מאוחר יותר.");
            return "error";
        }
    }

    @GetMapping("/editDegreeRequirement/{id}")
    public synchronized String showDegreeRequirementUpdateForm(@PathVariable("id") long id, Model model) {
        DegreeRequirement degreeRequirement = degreeRequirementsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid requirement Id:" + id));
        model.addAttribute("degreeRequirement", degreeRequirement);
        return "admin/edit-requirements";
    }

    @PostMapping("/updateDegreeRequirement")
    public synchronized String updateDegreeRequirement(@RequestParam("id") long id,
                                                       @Valid DegreeRequirement requirement,
                                                       BindingResult result, Model model) {

        if (result.hasErrors()) {
            requirement.setId(id);
            return "admin/edit-requirements";
        }

        String requirementName = requirement.getRequirementName();
        DegreeRequirement requirementFromDB = degreeRequirementsRepo.findByRequirementName(requirementName);
        if(requirementFromDB != null && requirementFromDB.getId() != id) {
            String existMessage = String.format("שם הדרישה \"%s\" כבר קיים. הזן שם אחר.", requirementName);
            model.addAttribute("message", existMessage);
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
    public synchronized String addDegreeRequirement(@Valid DegreeRequirement degreeRequirement, BindingResult result, Model model) {
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

        model.addAttribute("degreeRequirement", new DegreeRequirement());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());

        return "admin/requirements-page";
    }

    private DegreeRequirement getTotalDegreeRequirement(){
        String requirementName = "סך הכל נדרש";
        Integer totalCredits = degreeRequirementsRepo.sumTotalMandatoryCredits();
        return new DegreeRequirement(requirementName, totalCredits == null ? 0 : totalCredits);
    }
}