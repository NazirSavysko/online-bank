package controller.auto_loan;

import controller.payload.AutoLoanPayload;
import entity.AutoLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.AutoLoanService;
import utils.AutoLoanValidator;

import javax.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/auto-loans")
public final class AutoLoansController {

    private final AutoLoanService autoLoanService;
    private final Validator autoLoanValidator;

    @Autowired
    public AutoLoansController(
            final AutoLoanService autoLoanService,
            final @Qualifier("autoLoanValidator") Validator autoLoanValidator
    ) {
        this.autoLoanService = autoLoanService;
        this.autoLoanValidator = autoLoanValidator;
    }

    @GetMapping("list")
    public String getAutoLoansPage(final Model model) {
        model.addAttribute("autoLoans", this.autoLoanService.getAllAutoLoans());

        return "auto-loans/list";
    }

    @GetMapping("create")
    public String getCreateAutoLoanPage() {
        return "auto-loans/new_auto_loan";
    }

    @PostMapping("create")
    public String createAutoLoan(
            @Valid @ModelAttribute("autoLoanPayload") final AutoLoanPayload payload,
            final BindingResult bindingResult,
            final Model model
    ) {
        AutoLoanPayload autoLoanPayload = new AutoLoanPayload(
                payload.amount(),
                payload.termInMonths(),
                100,
                payload.holderPassportNumber()
        );
        this.autoLoanValidator.validate(autoLoanPayload, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "auto-loans/new_auto_loan";
        } else {
            final AutoLoan createdAutoLoan = this.autoLoanService.saveAutoLoan(
                    autoLoanPayload.holderPassportNumber(),
                    new BigDecimal(autoLoanPayload.amount()),
                    new BigDecimal(autoLoanPayload.amount()),
                    autoLoanPayload.termInMonths()
            );
            if (createdAutoLoan != null) {
                return "redirect:/auto-loans/%d".formatted(createdAutoLoan.getId());
            } else {
                return "auto-loans/new_auto_loan";
            }
        }
    }
}
