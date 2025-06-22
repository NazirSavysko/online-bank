package controller.auto_loan;

import controller.payload.AutoLoanPayload;
import entity.AutoLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import service.AutoLoanService;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/auto-loans/{autoLoanId:\\d+}")
public final class AutoLoanController {

    private final AutoLoanService autoLoanService;
    private final Validator autoLoanValidator;

    @Autowired
    public AutoLoanController(
            final AutoLoanService autoLoanService,
            final @Qualifier("autoLoanValidator") Validator autoLoanValidator
    ) {
        this.autoLoanService = autoLoanService;
        this.autoLoanValidator = autoLoanValidator;
    }

    @ModelAttribute("autoLoan")
    public AutoLoan autoLoan(@PathVariable("autoLoanId") final int autoLoanId) {
        return this.autoLoanService.getAutoLoanById(autoLoanId)
                .orElseThrow(() -> new NoSuchElementException("Auto loan with ID '%d' not found".formatted(autoLoanId)));
    }

    @GetMapping
    public String getAutoLoanPage() {
        return "auto-loans/auto_loan";
    }

    @GetMapping("/edit")
    public String getEditAutoLoanPage() {
        return "auto-loans/update_auto_loan";
    }

    @PutMapping("/edit")
    public String updateAutoLoan(
            @ModelAttribute(value = "autoLoan", binding = false) final AutoLoan autoLoan,
            @Valid @ModelAttribute("autoLoanPayload") final AutoLoanPayload payload,
            final BindingResult bindingResult,
            final Model model) {
        AutoLoanPayload autoLoanPayload = new AutoLoanPayload(
                autoLoan.getCreditAmount().intValue(),
                payload.termInMonths(),
                payload.currentAmount(),
                autoLoan.getCreditHolderPassportNumber()
        );
        this.autoLoanValidator.validate(autoLoanPayload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "auto-loans/update_auto_loan";
        } else {
            final AutoLoan updatedAutoLoan = this.autoLoanService.updateAutoLoan(
                    payload.currentAmount(),
                    payload.termInMonths(),
                    autoLoan.getId()
            );
            model.addAttribute("autoLoan", updatedAutoLoan);
            return "redirect:/auto-loans/%d".formatted(autoLoan.getId());
        }
    }
    @DeleteMapping("/delete")
    public String deleteAutoLoan(
            @ModelAttribute(value = "autoLoan", binding = false) final AutoLoan autoLoan,
            final Model model) {
        final boolean isDeleted = this.autoLoanService.deleteAutoLoan(autoLoan.getId());
        if (isDeleted) {
            return "redirect:/auto-loans/list";
        } else {
            model.addAttribute("error", "Failed to delete the auto loan. Please try again later.");
            return "auto-loans/auto_loan";
        }
    }
}
