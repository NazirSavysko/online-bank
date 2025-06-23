package controller.mortgage;

import controller.payload.mortgage.UpdateMortgagePayload;
import entity.Mortgage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import service.MortgageService;
import utils.MyOwnValidator;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/mortgages/{mortgageId}")
public final class MortgageController {

    private final String MORTGAGE_NOT_FOUND_MESSAGE = "Mortgage with ID '%d' not found.";

    private final MortgageService mortgageService;
    private final MyOwnValidator mortgageValidator;

    @Autowired
    public MortgageController(final MortgageService mortgageService,
                              final MyOwnValidator mortgageValidator) {
        this.mortgageService = mortgageService;
        this.mortgageValidator = mortgageValidator;
    }

    @ModelAttribute("mortgage")
    public Mortgage getMortgage(@PathVariable("mortgageId") final int mortgageId) {
        return this.mortgageService.getMortgageById(mortgageId)
                .orElseThrow(() -> new NoSuchElementException(MORTGAGE_NOT_FOUND_MESSAGE.formatted(mortgageId)));
    }

    @GetMapping
    public String getMortgagePage() {
        return "mortgages/mortgage";
    }

    @GetMapping("/edit")
    public String getEditMortgagePage() {
        return "mortgages/update_mortgage";
    }

    @PutMapping("/edit")
    public String updateMortgage(@ModelAttribute(value = "mortgage", binding = false) final Mortgage mortgage,
                                 @ModelAttribute("mortgagePayload") final UpdateMortgagePayload payload,
                                 final BindingResult bindingResult,
                                 final Model model) {
        this.mortgageValidator.validate(payload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "mortgages/update_mortgage";
        } else {
            boolean isUpdated = this.mortgageService.updateMortgage(
                    payload.mortgageHolderPassportNumber(),
                    payload.mortgageTerm(),
                    payload.amount(),
                    payload.currentAmount(),
                    mortgage.getId()
            );
            if (isUpdated) {
                return "redirect:/mortgages/%d".formatted(mortgage.getId());
            } else {
                final ObjectError error = new ObjectError("mortgagePayload", "user with this passport number does not exist");
                model.addAttribute("errors",error);
                return "mortgages/update_mortgage";
            }
        }
    }

    @DeleteMapping("/delete")
    public String deleteMortgage(@ModelAttribute(value = "mortgage", binding = false) final Mortgage mortgage) {
        this.mortgageService.deleteMortgage(mortgage.getId());

        return "redirect:/mortgages/list";
    }

}
