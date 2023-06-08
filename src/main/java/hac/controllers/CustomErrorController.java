package hac.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
public class CustomErrorController {
    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("error", "Some error occured");
        return "error";
    }
}
