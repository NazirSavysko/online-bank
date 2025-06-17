package controller.user;

import controller.payload.user.NewUserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users/")
public final class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("list")
    public String getAllUsers(final Model model) {
        model.addAttribute("users", this.userService.getAllUsers());
        this.userService.getAllUsers().forEach(System.out::println);
        return "users/list";
    }

    @GetMapping("create")
    public String getCreateUserPage() {
        return "users/create";
    }

    @PostMapping("create")
    public String createUser(
            @Valid final NewUserPayload payload,
            final BindingResult bindingResult,
            final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "users/create";
        }else {
            final boolean isCreated = this.userService.saveUser(
                    payload.passportNumber(),
                    payload.username(),
                    payload.gender(),
                    payload.dateOfBirth()
            );
            if (isCreated) {
                return "redirect:users/%s".formatted(payload.passportNumber());
            }else {
                model.addAttribute("errors", "User with this passport number already exists.");
                return "users/create";
            }
        }
    }
}
