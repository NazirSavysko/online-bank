package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.UserService;

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
        return "users/list";
    }
}
