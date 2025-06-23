package controller.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static java.lang.System.err;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public String handleException(final @NotNull SQLException e) {
        err.println("SQLException occurred: " + e.getMessage());

        return "error/500";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(final @NotNull NoSuchElementException e,
                                               final @NotNull HttpServletResponse response,
                                               final @NotNull Model model) {
        response.setStatus(NOT_FOUND.value());
        ObjectError error = new ObjectError("notFound", e.getMessage());
        model.addAttribute("errors",error);

        return "error/404";
    }


}
