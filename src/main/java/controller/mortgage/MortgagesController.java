package controller.mortgage;

import controller.payload.mortgage.NewMortgagePayload;
import dto.MortgageDTO;
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
import service.MortgageService;

import javax.validation.Valid;


@Controller
@RequestMapping("/mortgages")
public final class MortgagesController {

    private final MortgageService mortgageService;
    private final Validator mortgageValidator;

    public MortgagesController(final MortgageService mortgageService,
                               @Qualifier("myOwnValidator") final Validator mortgageValidator) {
        this.mortgageService = mortgageService;
        this.mortgageValidator = mortgageValidator;
    }

    @GetMapping("list")
    public String getAllMortgages(final Model model) {
        model.addAttribute("mortgages", this.mortgageService.getAllMortgages());

        return "mortgages/list";
    }

    @GetMapping("create")
    public String getCreateMortgagePage() {
        return "mortgages/new_mortgage";
    }

    @PostMapping("create")
    public String createMortgage(
            @Valid @ModelAttribute("mortgagePayload") final NewMortgagePayload payload,
            final BindingResult bindingResult,
            final Model model
    ) {
        this.mortgageValidator.validate(payload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());

            return "mortgages/new_mortgage";
        } else {
            try {
                final MortgageDTO createdMortgage = this.mortgageService.saveMortgage(
                        payload.mortgageHolderPassportNumber(),
                        payload.mortgageTerm(),
                        payload.amount(),
                        payload.currentAmount()
                );

                return "redirect:/mortgages/%d".formatted(createdMortgage.id());
            } catch (IllegalArgumentException e) {
                final ObjectError error = new ObjectError("mortgagePayload", e.getMessage());
                model.addAttribute("errors", error);

                return "mortgages/new_mortgage";
            }
        }
    }
}
