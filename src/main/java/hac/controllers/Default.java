package hac.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && !auth.getName().equals("anonymousUser")) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin";
                } else if (authority.getAuthority().equals("ROLE_USER")) {
                    return "redirect:/user";
                }
            }
        }

        return "login"; // Otherwise, show them the login page
    }
//    /** User zone index. */
//    @GetMapping("/user")
//    public String userIndex() {
//        return "user/course-catalog";
//    }
//
//    /** Administration zone index.
//     * Note that we can access current logged user just by adding the Principal
//     * parameter
//     */
//    @RequestMapping("/admin")
//    public String adminIndex(Principal principal) {
//        System.out.println("Current logged user details: " + " (" + principal + ")" );
//        return "manage-courses";
//    }

    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {

        //logger.error("Exception during execution of SpringSecurity application", ex);
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");

        model.addAttribute("errorMessage", errorMessage);
        return "403";
    }
}
