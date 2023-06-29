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




@Controller
@RequestMapping("/user")
public class StudentController {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserCoursesRepo userCoursesRepo;

    @Autowired
    private DegreeRequirementsRepo degreeRequirementsRepo;

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

    @GetMapping("/userCatalog")
    public String userCatalog(Model model, Principal principal) {
        List<Long> userCoursesIds = new ArrayList<>();
        for (UserCourses userCourse : userCoursesRepo.findByUsername(principal.getName())) {
            userCoursesIds.add(userCourse.getCourse().getId());
        }

        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCoursesIds", userCoursesIds);
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        return "user/course-catalog";
    }

    private DegreeRequirement getTotalDegreeRequirement(){
        String requirementName = "סך הכל";
        Integer totalCredits = degreeRequirementsRepo.sumTotalMandatoryCredits();
        return new DegreeRequirement(requirementName, totalCredits == null ? 0 : totalCredits);
    }

    @PostMapping("/addToStudentList")
    public synchronized String addCourseToStudentList(@RequestParam("id") long id, Model model, Principal principal) {
        Course newCourse = courseRepo
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid user Id:" + id)
                );
        UserCourses existingUserCourses = userCoursesRepo.findByCourseAndUsername(newCourse, principal.getName());

        if (existingUserCourses == null) {
            userCoursesRepo.save( new UserCourses(newCourse,principal.getName(),null) );
            model.addAttribute("message", "הקורס נוסף בהצלחה");
        }

        List<Long> userCoursesIds = new ArrayList<>();
        for (UserCourses userCourse : userCoursesRepo.findByUsername(principal.getName())) {
            userCoursesIds.add(userCourse.getCourse().getId());
        }
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
        model.addAttribute("userCoursesIds", userCoursesIds);
        return "user/course-catalog";
    }

    @PostMapping("/deleteCourse")
    public synchronized String deleteCourse(@RequestParam("id") long id, Model model) {
        UserCourses userCourse = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        userCoursesRepo.delete(userCourse);
        model.addAttribute("message", "Course deleted successfully.");
        return "redirect:/user/myCourses";
    }

    @PostMapping("/deleteCourseFromCatalog")
    public synchronized String deleteCourseFromCatalog(@RequestParam("id") long id, Model model) {
        UserCourses userCourse = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        userCoursesRepo.delete(userCourse);
        model.addAttribute("message", "Course deleted successfully.");
        return "redirect:/user/userCatalog";
    }

    @PostMapping("/editCourse")
    public String editCourse(@RequestParam("id") long id, Model model) {
        UserCourses userCourses = userCoursesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("userCourses",userCourses);
        return "user/edit-user-course";
    }

    @PostMapping("/updateCourse")
    public synchronized String updateCourse(@RequestParam("id") long id, @Valid UserCourses userCourses, BindingResult result) {
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