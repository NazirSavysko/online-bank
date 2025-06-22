package controller.debit_card;

import controller.payload.DebitCardPayload;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.DebitCardService;
import utils.DebitCardValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/debit-cards")
public final class DebitCardsController {

    private final DebitCardService debitCardService;
    private final DebitCardValidator debitCardValidator;

    @Autowired
    public DebitCardsController(final DebitCardService debitCardService,
                                final DebitCardValidator debitCardValidator) {
        this.debitCardService = debitCardService;
        this.debitCardValidator = debitCardValidator;
    }

    @GetMapping("list")
    public String getDebitCardsPage(final Model model) {
        model.addAttribute("debitCards", this.debitCardService.getAllDebitCards());
        return "debit-cards/list";
    }

    @GetMapping("create")
    public String getCreateDebitCardPage() {
        return "debit-cards/new_debit_card";
    }

    @PostMapping("create")
    public String createDebitCard(
            @Valid @ModelAttribute("debitCardPayload") final DebitCardPayload payload,
            final BindingResult bindingResult,
            final Model model
    ) {
        this.debitCardValidator.validate(payload, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "debit-cards/new_debit_card";
        } else {
            final DebitCard debitCard = this.debitCardService.saveDebitCard(
                    payload.cardHolderPassportNumber()
            );
            if (debitCard != null) {
                return "redirect:/debit-cards/%s".formatted(debitCard.getCardNumber());
            } else {
                ObjectError error = new ObjectError("debitCardPayload", "Invalid number of holder's passport");
                model.addAttribute("errors",error);
                return "debit-cards/new_debit_card";
            }
        }
    }
}
