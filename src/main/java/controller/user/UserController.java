package controller.user;

import controller.payload.user.UpdateUserPayload;
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
@RequestMapping("/users/{userId:\\d+}")
public class UserController {

    private final String USER_NOT_FOUND_MESSAGE = "User with id %d not found.";

    private final UserService userService;
    private final Validator userValidator;

    @Autowired
    public UserController(final UserService userService,
                          final @Qualifier("myOwnValidator") Validator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @ModelAttribute("user")
    public User user(@PathVariable("userId") final int id) {
        return this.userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND_MESSAGE.formatted(id)));
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
    public String updateUser(@ModelAttribute(value = "user", binding = false) final User user,
                             @Valid @ModelAttribute("userPayload") final UpdateUserPayload payload,
                             final BindingResult bindingResult,
                             final Model model) {
        this.userValidator.validate(payload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "users/update_user";
        } else {
            final boolean isUpdated = this.userService.updateUser(
                    payload.passportNumber(),
                    user.getPassportNumber(),
                    payload.userName(),
                    payload.gender(),
                    payload.dateOfBirth(),
                    user.getId()
            );
            if (isUpdated) {
                return "redirect:/users/%d".formatted(user.getId());
            } else {
                final ObjectError error = new ObjectError("user", "User with this passport number already exists.");
                model.addAttribute("error", error);

                return "users/update_user";
            }

        }
    }

    @DeleteMapping("/delete")
    public String deleteUser(@ModelAttribute(value = "user") final User user) {
        this.userService.deleteUser(user.getId());

        return "redirect:/users/list";
    }
}
