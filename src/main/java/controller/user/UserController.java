package controller.user;

import controller.payload.UserPayload;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import javax.validation.Valid;
import java.util.NoSuchElementException;


@Controller
@RequestMapping("/users/{userPassportNumber}")
public class UserController {

    private final UserService userService;
    private final Validator userValidator;

    @Autowired
    public UserController(
            final UserService userService,
            final @Qualifier("userValidator") Validator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @ModelAttribute("user")
    public User user(@PathVariable("userPassportNumber") String userPassportNumber) {
        User user = this.userService.getUserByPassportNumber(userPassportNumber)
                .orElseThrow(() -> new NoSuchElementException("User with passport number '%s' not found".formatted(userPassportNumber)));
        System.out.println("User found: " + user.getUserName() +  user.getPassportNumber() + user.getDateOfBirth() +  user.getGender());
        return user;
    }

    @GetMapping
    public String getUserPage() {
        return "users/user";
    }

    @GetMapping("/edit")
    public String getEditUserPage() {
        return "users/update_user";
    }

    @PutMapping("/edit")
    public String updateUser(
            @ModelAttribute(value = "user", binding = false) final User user,
            @Valid @ModelAttribute("userPayload") final UserPayload payload,
            final BindingResult bindingResult,
            final Model model) {
        this.userValidator.validate(payload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "users/update_user";
        } else {
            final boolean isUpdated = this.userService.updateUser(
                    payload.passportNumber(),
                    payload.userName(),
                    payload.gender(),
                    payload.dateOfBirth()
            );
            if (isUpdated) {
                return "redirect:/users/%s".formatted(payload.passportNumber());
            } else {
                final ObjectError error = new ObjectError("passportNumber", "the server could not update the user try again later.");
                model.addAttribute("errors", error);
                return "users/update_user";
            }
        }
    }

    @DeleteMapping("/delete")
    public String deleteUser(
            @ModelAttribute(value = "user") final User user) {
        final boolean isDeleted = this.userService.deleteUser(user.getPassportNumber());
        if (isDeleted){
            return "redirect:/users/list";
        }else {
            throw new RuntimeException("The server could not delete the user, try again later.");
        }
    }
}
