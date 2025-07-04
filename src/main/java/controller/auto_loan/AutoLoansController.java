package controller.auto_loan;

import controller.payload.auto_loan.NewAutoLoanPayload;
import dto.AutoLoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.AutoLoanService;

import javax.validation.Valid;

@Controller
@RequestMapping("/auto-loans")
public final class AutoLoansController {

    private final AutoLoanService autoLoanService;
    private final Validator autoLoanValidator;

    @Autowired
    public AutoLoansController(
            final AutoLoanService autoLoanService,
            final @Qualifier("myOwnValidator") Validator autoLoanValidator
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
    public String createAutoLoan(@Valid @ModelAttribute("autoLoanPayload") final NewAutoLoanPayload payload,
                                 final BindingResult bindingResult,
                                 final Model model) {
        this.autoLoanValidator.validate(payload, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "auto-loans/new_auto_loan";
        } else {
            try {
                final AutoLoanDTO createdAutoLoan = this.autoLoanService.saveAutoLoan(
                        payload.holderPassportNumber(),
                        payload.amount(),
                        payload.currentAmount(),
                        payload.termInMonths()
                );

                return "redirect:/auto-loans/%d".formatted(createdAutoLoan.id());
            } catch (IllegalArgumentException e) {
                ObjectError error = new ObjectError("autoLoanPayload", e.getMessage());
                model.addAttribute("errors", error);

                return "auto-loans/new_auto_loan";
            }
        }
    }
}
