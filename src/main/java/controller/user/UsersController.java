package controller.user;

import controller.payload.UserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users/")
public final class UsersController {

    private final UserService userService;
    private final Validator userValidator;

    @Autowired
    public UsersController(
            final UserService userService,
            final @Qualifier("userValidator") Validator userValidator
    ) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("list")
    public String getAllUsers(final Model model) {
        model.addAttribute("users", this.userService.getAllUsers());
        this.userService.getAllUsers().forEach(System.out::println);
        return "users/list";
    }

    @GetMapping("create")
    public String getCreateUserPage() {
        return "users/new_user";
    }

    @PostMapping("create")
    public String createUser(
            @Valid @ModelAttribute("userPayload") final UserPayload payload,
            final BindingResult bindingResult,
            final Model model) {
        this.userValidator.validate(payload, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "users/new_user";
        } else {
            final boolean isCreated = this.userService.saveUser(
                    payload.passportNumber(),
                    payload.userName(),
                    payload.gender(),
                    payload.dateOfBirth()
            );
            if (isCreated) {
                return "redirect:%s".formatted(payload.passportNumber());
            } else {
                final ObjectError error = new ObjectError("passportNumber", "User with this passport number already exists.");
                model.addAttribute("errors", error);
                return "users/new_user";
            }
        }
    }
}
