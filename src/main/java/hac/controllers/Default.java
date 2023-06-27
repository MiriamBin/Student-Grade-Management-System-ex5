package hac.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Default {
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

        return "login";
    }
}
