package hac.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom error controller for handling errors
 */
@Controller
public class CustomErrorController {

    /**
     * handle 404 error page
     * @return 404 error page - access denied
     */
    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }

    /**
     * handle 404 error page
     * @param ex exception
     * @param model model
     * @return 404 error - page not found
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
