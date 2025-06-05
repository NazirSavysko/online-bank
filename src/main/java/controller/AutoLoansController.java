package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.AutoLoanService;

@Controller
@RequestMapping("/auto-loans")
public final class AutoLoansController {

    private final AutoLoanService autoLoanService;

    @Autowired
    public AutoLoansController(final AutoLoanService autoLoanService) {
        this.autoLoanService = autoLoanService;
    }

    @GetMapping("list")
    public String getAutoLoansPage(final Model model) {
        model.addAttribute("autoLoans", this.autoLoanService.getAllAutoLoans());

        return "auto-loans/list";
    }
}
