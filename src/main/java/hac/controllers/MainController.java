package hac.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Main controller for handling main page and login page
 *
 */
@Controller
public class MainController {
    /**
     * handle main page request - landing page
     * @return landing page
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * handle login page request - redirect to admin page if admin is logged in,
     * redirect to user page if user is logged in
     * @return login page
     */
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

        return "login";
    }
}
