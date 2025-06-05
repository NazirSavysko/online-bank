package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.MortgageService;

@Controller
@RequestMapping("/mortgages")
public final class MortgagesController {

    private final MortgageService mortgageService;

    public MortgagesController(MortgageService mortgageService) {
        this.mortgageService = mortgageService;
    }


    @GetMapping("list")
    public String getAllMortgages(final Model model) {
        model.addAttribute("mortgages", this.mortgageService.getAllMortgages());
        return "mortgages/list";
    }
}
