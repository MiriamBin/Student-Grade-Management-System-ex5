package hac.controllers;

import hac.beans.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Student controller for handling student page
 */
@Controller
@RequestMapping("/user")
public class StudentController {

    final String invalidId = "מספר ID של הקורס לא חוקי";
    final String courseAddedSuccessfully = "הקורס נוסף בהצלחה";
    final String courseDeletedSuccessfully = "הקורס נמחק בהצלחה";
    final String coursesModelAttribute = "courses";
    final String userCoursesModelAttribute = "userCourses";
    final String degreeRequirementsModelAttribute = "degreeRequirements";
    final String totalDegreeRequirementModelAttribute = "totalDegreeRequirement";

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @Autowired
    private DegreeRequirementsRepo degreeRequirementsRepo;

    /**
     * handle user home page request - student page
     * @param model model for student page - contains student courses status
     * @param principal principal for getting username
     * @return student page
     */
    @GetMapping("")
    public String userHomePage(Model model, Principal principal) {

        Map<String, List<String>> studentStatus = new LinkedHashMap<>();
        List<UserCourses> userCourses = userCoursesRepo.findByUsername(principal.getName());
        List<String> studentCoursesStatus = calculateStudentCoursesStatus(userCourses);
        studentStatus.put("תואר", studentCoursesStatus);

        String username = principal.getName();
        List<DegreeRequirement> requirementTypes = degreeRequirementsRepo.findAll();
        for (DegreeRequirement requirement : requirementTypes) {
            String requirementName = requirement.getRequirementName();
            List<UserCourses> requirementTypeCourses = userCoursesRepo.findByUsernameAndRequirementType(username, requirementName);
            List<String> requirementTypeStatus = calculateStudentCoursesStatus(requirementTypeCourses);
            studentStatus.put(requirementName, requirementTypeStatus);
        }
        model.addAttribute("studentStatus", studentStatus);
        return "user/home-page";
    }

    /**
     * handle user courses page request - student courses page
     * @param courses model for student courses page - contains student courses
     * @return student courses page
     */
    private List<String> calculateStudentCoursesStatus(List<UserCourses> courses) {
        double totalGradePoints = 0.0;
        int completedCredits = 0;
        int inProgressCredits = 0;
        int totalCredits = 0;

        for (UserCourses course : courses) {
            if (course.getGrade() != null) {
                totalGradePoints += (course.getGrade() * course.getCourse().getCredit());
                completedCredits += course.getCourse().getCredit();
            } else {
                inProgressCredits += course.getCourse().getCredit();
            }
            totalCredits += course.getCourse().getCredit();
        }

        List<String> StudentCoursesStatus = new ArrayList<>();
        StudentCoursesStatus.add(String.valueOf(completedCredits));
        StudentCoursesStatus.add(String.valueOf(inProgressCredits));
        StudentCoursesStatus.add(String.valueOf(totalCredits));
        StudentCoursesStatus.add(String.format("%.2f", totalGradePoints / (completedCredits == 0 ? 1 : completedCredits)));
        return StudentCoursesStatus;
    }

    /**
     *  handle user catalog page request - student catalog page
     * @param model model for student catalog page - contains courses, user courses ids,
     * degree requirements and total degree requirement
     * @param principal principal for getting username
     * @return student catalog page
     */
    @GetMapping("/userCatalog")
    public String userCatalog(Model model, Principal principal) {
        List<Long> userCoursesIds = new ArrayList<>();
        for (UserCourses userCourse : userCoursesRepo.findByUsername(principal.getName())) {
            userCoursesIds.add(userCourse.getCourse().getId());
        }

        model.addAttribute(coursesModelAttribute, courseRepo.findAll());
        model.addAttribute("userCoursesIds", userCoursesIds);
        model.addAttribute(degreeRequirementsModelAttribute, degreeRequirementsRepo.findAll());
        model.addAttribute(totalDegreeRequirementModelAttribute, getTotalDegreeRequirement());
        return "user/course-catalog";
    }

    private DegreeRequirement getTotalDegreeRequirement(){
        String requirementName = "סך הכל";
        Integer totalCredits = degreeRequirementsRepo.sumTotalMandatoryCredits();
        return new DegreeRequirement(requirementName, totalCredits == null ? 0 : totalCredits);
    }

    /**
     * handle user my courses page request - student my courses page
     * @param id course id for deleting course
     * @param model model for student my courses page - contains user courses and total degree requirement
     * @param principal principal for getting username
     * @return student my courses page
     */
    @PostMapping("/addToStudentList")
    public synchronized String addCourseToStudentList(@RequestParam("id") long id, Model model, Principal principal) {
        Course newCourse = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(invalidId + id)
                );
        UserCourses existingUserCourses = userCoursesRepo.findByCourseAndUsername(newCourse, principal.getName());

        if (existingUserCourses == null) {
            userCoursesRepo.save( new UserCourses(newCourse,principal.getName(),null) );
            model.addAttribute("message", courseAddedSuccessfully);
        }

        List<Long> userCoursesIds = new ArrayList<>();
        for (UserCourses userCourse : userCoursesRepo.findByUsername(principal.getName())) {
            userCoursesIds.add(userCourse.getCourse().getId());
        }
        model.addAttribute(coursesModelAttribute, courseRepo.findAll());
        model.addAttribute(degreeRequirementsModelAttribute, degreeRequirementsRepo.findAll());
        model.addAttribute(totalDegreeRequirementModelAttribute, getTotalDegreeRequirement());
        model.addAttribute("userCoursesIds", userCoursesIds);
        return "user/course-catalog";
    }

    /**
     * handle user my courses page request - student my courses page
     * @param id course id for deleting course
     * @param model model for student my courses page - contains user courses and total degree requirement
     * @return student my courses page
     */
    @PostMapping("/deleteCourse")
    public synchronized String deleteCourse(@RequestParam("id") long id, Model model) {
        UserCourses userCourse = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        userCoursesRepo.delete(userCourse);
        model.addAttribute("message", courseDeletedSuccessfully);
        return "redirect:/user/myCourses";
    }

    /**
     * handle user my courses page request - student my courses page
     * @param id course id for deleting course
     * @param model model for student my courses page - contains user courses and total degree requirement
     * @return student my courses page
     */
    @PostMapping("/deleteCourseFromCatalog")
    public synchronized String deleteCourseFromCatalog(@RequestParam("id") long id, Model model) {
        UserCourses userCourse = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(invalidId + id));
        userCoursesRepo.delete(userCourse);
        model.addAttribute("message", courseDeletedSuccessfully);
        return "redirect:/user/userCatalog";
    }

    /**
     * handle user edit course page request - student edit course page
     * @param id course id for editing course
     * @param model model for student edit course page - contains user courses
     * @return student edit course page
     */
    @PostMapping("/editCourse")
    public String editCourse(@RequestParam("id") long id, Model model) {
        UserCourses userCourses = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(invalidId + id));
        model.addAttribute(userCoursesModelAttribute,userCourses);
        return "user/edit-user-course";
    }

    /**
     * handle user update course page request - student update course page
     * @param id course id for updating course
     * @param userCourses user courses for updating course
     * @param result result for updating course
     * @return student my courses page
     */
    @PostMapping("/updateCourse")
    public synchronized String updateCourse(@RequestParam("id") long id, @Valid UserCourses userCourses, BindingResult result) {
        if (result.hasErrors()) {
            return "user/edit-user-course";
        }

        // Retrieve the course from the database based on the given id
        UserCourses existingUserCourses = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(invalidId + id));

        // Update the grade value
        existingUserCourses.setGrade(userCourses.getGrade());

        // Save the updated course in the database
        userCoursesRepo.save(existingUserCourses);

        return "redirect:/user/myCourses";
    }


    /**
     * handle user my courses page request - student my courses page
     * @param model model for student my courses page - contains user courses
     * @param principal principal for getting username
     * @return student my courses page
     */
    @GetMapping("/myCourses")
    public String myCourses(Model model, Principal principal) {
        model.addAttribute(coursesModelAttribute, courseRepo.findAll());
        model.addAttribute(userCoursesModelAttribute, userCoursesRepo.findAll());
        model.addAttribute(userCoursesModelAttribute, userCoursesRepo.findByUsername(principal.getName()));
        return "/user/my-courses";
    }
}