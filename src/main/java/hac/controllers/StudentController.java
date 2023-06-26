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
import java.util.List;
import java.util.Map;

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
        List<UserCourses> userCourses = userCoursesRepo.findByUsername(principal.getName());
        Double sum = 0.0;
        Integer count = 0;
        Integer countCompletedCredits = 0;
        Integer countInProgressCredits = 0;
        Integer countRemainingCredits = 0;
        Integer countTotalCredits = 0;

        for (UserCourses uc : userCourses) {
            if (uc.getGrade() != null) {
                sum += (uc.getGrade() * uc.getCourse().getCredit());
                count++;
                countCompletedCredits += uc.getCourse().getCredit();
            } else {
                countInProgressCredits += uc.getCourse().getCredit();
            }
            countTotalCredits += uc.getCourse().getCredit();
        }

        Double gpa = sum / countCompletedCredits;
        gpa = Double.isNaN(gpa) ? 0.0 : gpa;
        model.addAttribute("gpa", String.format("%.2f", gpa));


//        Double gpa = userCoursesRepo.calculateGPAForUser(principal.getName());
//        model.addAttribute("gpa", gpa);
////        Integer count = userCoursesRepo.countCoursesWithGradeForUser(principal.getName()).intValue();
////        model.addAttribute("count", count);

        return "user/home-page";
    }

    @GetMapping("/userCatalog")
    public String userCatalog(Model model, Principal principal) {
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("userCourses", userCoursesRepo.findByUsername(principal.getName()));
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
        model.addAttribute("degreeRequirements", degreeRequirementsRepo.findAll());
        model.addAttribute("totalDegreeRequirement", getTotalDegreeRequirement());
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