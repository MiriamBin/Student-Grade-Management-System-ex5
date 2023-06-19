package hac.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class Default {
    //private static Logger logger = LoggerFactory.getLogger(Default.class);

    /** Home page. */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** User zone index. */
    @GetMapping("/user")
    public String userIndex() {
        return "user/course-catalog";
    }

    /** Administration zone index.
     * Note that we can access current logged user just by adding the Principal
     * parameter
     */
    @RequestMapping("/admin")
    public String adminIndex(Principal principal) {
        System.out.println("Current logged user details: " + " (" + principal + ")" );
        return "admin/ManageCourses";
    }
}
