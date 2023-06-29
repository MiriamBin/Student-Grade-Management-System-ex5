package hac.controllers;

import hac.beans.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Admin controller class for admin pages and actions (add, delete, update courses)
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    final String errorMessages = "קרתה תקלה, אנא נסה שנית מאוחר יותר";
    final String courseInUse = "לא ניתן למחוק קורס שהוקצה לסטודנטים";
    final String invalidId = "מספר ID של הקורס לא חוקי";

    /**
     * Autowired course repository
     */
    @Autowired
    private CourseRepo courseRepo;

    /**
     * Autowired user courses repository
     */
    @Autowired
    private UserCoursesRepo userCoursesRepo;

    /**
     * Autowired degree requirements repository
     */
    @Autowired
    private DegreeRequirementsRepo degreeRequirementsRepo;

    /**
     * default page for admin
     * @return admin home page
     */
    @GetMapping("")
    public String adminIndex() {
        return "admin/home-page";
    }

    /**
     * manage courses page for admin
     * @param model model - contains courses and degree requirements
     * @return manage courses page
     */
    @GetMapping("/manageCourses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/manage-courses";
    }

    /**
     * add course to database
     * @param course course to add
     * @param result result of validation
     * @param model model - contains courses and degree requirements
     * @return manage courses page
     */
    @PostMapping("/addCourse")
    public synchronized String addCourse(@Valid Course course, BindingResult result, Model model) {
        try {
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
        } catch (Exception e) {
            model.addAttribute("error", errorMessages);
            return "error";
        }
        return "admin/manage-courses";
    }


    /**
     * delete course from database
     * @param id course id to delete
     * @param model model - contains courses
     * @return manage courses page
     */
    @PostMapping("/delete")
    public synchronized String deleteCourse(@RequestParam("id") long id, Model model) {
        try {
            Course course = courseRepo
                    .findById(id)
                    .orElseThrow(
                            () -> new IllegalArgumentException(invalidId + id)
                    );

            List<UserCourses> userCourses = userCoursesRepo.findByCourseId(id);
            if(!userCourses.isEmpty()) {
                model.addAttribute("existMessage", courseInUse);
                model.addAttribute("course", new Course());
                model.addAttribute("courses", courseRepo.findAll());
                model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());

                return "admin/manage-courses";
            } else {
                courseRepo.delete(course);
            }
        } catch (Exception e) {
            model.addAttribute("error", errorMessages);
            return "error";
        }

        model.addAttribute("courses", courseRepo.findAll());
        return "redirect:/admin/manageCourses";
    }

    /**
     * show update form for course to update course details in database (course name, degree requirements)
     * @param id course id to update
     * @param model model - contains course and degree requirements
     * @return edit course page
     */
    @GetMapping("/editCourse/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        try{
            Course course = courseRepo.findById(id).orElseThrow(() -> new IllegalArgumentException(invalidId + id));
            model.addAttribute("course", course);
            model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
            return "admin/edit-courses";
        } catch (Exception e) {
        model.addAttribute("error", errorMessages);
        return "error";
        }
    }

    /**
     * update course details in database (course name, degree requirements)
     * @param id course id to update
     * @param course course to update
     * @param result  result of validation
     * @param model model - contains course and degree requirements
     * @return edit course page
     */
    @PostMapping("/update")
    public synchronized String updateCourse(@RequestParam("id") long id, @Valid Course course, BindingResult result, Model model) {
        try {
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
        } catch (Exception e) {
            model.addAttribute("error", errorMessages);
            return "error";
        }
        return "redirect:/admin/manageCourses";
    }

    /**
     * show new course form to add new course to database
     * @param model model - contains course and degree requirements
     * @return manage courses page
     */
    @GetMapping("/newCourse")
    public String newCourse(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAllRequirementNames());
        return "admin/manage-courses";
    }

    /**
     * show requirements page - contains all degree requirements
     * @param model model - contains degree requirements
     * @return requirements page
     */
    @GetMapping("/requirements")
    public String showRequirementsPage(Model model) {
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        return "admin/requirements-page";
    }

    /**
     * get total degree requirement
     * @param id degree requirement id
     * @param model model - contains degree requirements
     * @return requirements page
     */
    @PostMapping("/deleteRequirements")
    public synchronized  String deleteRequirements(@RequestParam("id") long id, Model model) {
        try {
            DegreeRequirement requirement = degreeRequirementsRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid DegreeRequirements Id:" + id));
            degreeRequirementsRepo.delete(requirement);
            return "redirect:/admin/requirements";
        } catch (Exception e) {
            model.addAttribute("error", errorMessages);
            return "error";
        }
    }

    /**
     * show update form for degree requirement to update degree requirement details in database
     * (requirement name, requirement points)
     * @param id degree requirement id to update
     * @param model model - contains degree requirement
     * @return edit degree requirement page
     */
    @GetMapping("/editDegreeRequirement/{id}")
    public synchronized String showDegreeRequirementUpdateForm(@PathVariable("id") long id, Model model) {
        try {
            DegreeRequirement degreeRequirement = degreeRequirementsRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid requirement Id:" + id));
            model.addAttribute("degreeRequirement", degreeRequirement);
            return "admin/edit-requirements";
        } catch (Exception e) {
            model.addAttribute("error", errorMessages);
            return "error";
        }
    }

    /**
     * update degree requirement details in database (requirement name, requirement points)
     * @param id degree requirement id to update
     * @param requirement degree requirement to update
     * @param result result of validation
     * @param model model - contains degree requirement
     * @return edit degree requirement page
     */
    @PostMapping("/updateDegreeRequirement")
    public synchronized String updateDegreeRequirement(@RequestParam("id") long id,
                                                       @Valid DegreeRequirement requirement,
                                                       BindingResult result, Model model) {
        try {
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
        } catch (Exception e) {
            model.addAttribute("error", errorMessages);
            return "error";
        }
    }

    /**
     * show new degree requirement form to add new degree requirement to database
     * @param model model - contains degree requirement
     * @return requirements page
     */
    @GetMapping("/newDegreeRequirement")
    public String newDegreeRequirement(Model model) {
        model.addAttribute("degreeRequirement", new DegreeRequirement());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        return "admin/requirements-page";
    }

    /**
     * add new degree requirement to database (requirement name, requirement points)
     * @param degreeRequirement degree requirement to add to database
     * @param result result of validation
     * @param model model - contains degree requirement
     * @return requirements page
     */
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

    /**
     * get total degree requirement
     * @return total degree requirement
     */
    private DegreeRequirement getTotalDegreeRequirement(){
        String requirementName = "סך הכל נדרש";
        Integer totalCredits = degreeRequirementsRepo.sumTotalMandatoryCredits();
        return new DegreeRequirement(requirementName, totalCredits == null ? 0 : totalCredits);
    }
}