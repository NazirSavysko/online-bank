package controller.auto_loan;

import controller.payload.auto_loan.PaymentAutoLoanPayload;
import controller.payload.auto_loan.UpdateAutoLoanPayload;
import dto.AutoLoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import service.AutoLoanService;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/auto-loans/{autoLoanId:\\d+}")
public final class AutoLoanController {

    private static final String AUTO_LOAN_NOT_FOUND_MESSAGE = "Auto loan with ID '%d' not found.";

    private final AutoLoanService autoLoanService;
    private final Validator autoLoanValidator;

    @Autowired
    public AutoLoanController(
            final AutoLoanService autoLoanService,
            @Qualifier("myOwnValidator") final Validator autoLoanValidator
    ) {
        this.autoLoanService = autoLoanService;
        this.autoLoanValidator = autoLoanValidator;
    }

    @ModelAttribute("autoLoan")
    public AutoLoanDTO autoLoan(@PathVariable("autoLoanId") final int autoLoanId) {
        return this.autoLoanService.getAutoLoanById(autoLoanId)
                .orElseThrow(() -> new NoSuchElementException(AUTO_LOAN_NOT_FOUND_MESSAGE.formatted(autoLoanId)));
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
            @ModelAttribute(value = "autoLoan", binding = false) final AutoLoanDTO autoLoan,
            @Valid @ModelAttribute("autoLoanPayload") final UpdateAutoLoanPayload payload,
            final BindingResult bindingResult,
            final Model model) {
        this.autoLoanValidator.validate(payload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());

            return "auto-loans/update_auto_loan";
        } else {
            final boolean updatedAutoLoan = this.autoLoanService.updateAutoLoan(
                    payload.holderPassportNumber(),
                    payload.amount(),
                    payload.currentAmount(),
                    payload.termInMonths(),
                    autoLoan.id()
            );

            if (updatedAutoLoan) {
                return "redirect:/auto-loans/%d".formatted(autoLoan.id());
            } else {
                final ObjectError error = new ObjectError("autoLoan", " holder with this passport number does not exist ");
                model.addAttribute("errors", error);

                return "auto-loans/update_auto_loan";
            }
        }
    }

    @DeleteMapping("/delete")
    public String deleteAutoLoan(@ModelAttribute(value = "autoLoan", binding = false) final AutoLoanDTO autoLoan) {
        this.autoLoanService.deleteAutoLoan(autoLoan.id());

        return "redirect:/auto-loans/list";
    }

    @GetMapping("/payment")
    public String getPayAutoLoanPage() {
        return "auto-loans/payment";
    }

    @PostMapping("/payment")
    public String payAutoLoan(
            @ModelAttribute("paymentPayload") final PaymentAutoLoanPayload paymentPayload,
            final BindingResult bindingResult,
            final Model model) {
        this.autoLoanValidator.validate(paymentPayload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());

            return "auto-loans/payment";
        } else {
            try {
                this.autoLoanService.payAutoLoan(paymentPayload.id(), paymentPayload.amount());

                return "redirect:/auto-loans/list";
            } catch (final IllegalArgumentException e) {
                ObjectError error = new ObjectError("paymentPayload", e.getMessage());
                model.addAttribute("errors", error);

                return "auto-loans/payment";
            }
        }
    }
}
