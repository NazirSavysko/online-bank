package controller.debit_card;

import controller.payload.DebitCardPayload;
import controller.payload.UpdateDebitCardPayload;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.DebitCardService;
import utils.DebitCardValidator;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/debit-cards/{cardNumber}")
public final class DebitCardController {

    private final DebitCardService debitCardService;
    private final DebitCardValidator debitCardValidator;

    @Autowired
    public DebitCardController(final DebitCardService debitCardService,
                               final DebitCardValidator debitCardValidator) {
        this.debitCardService = debitCardService;
        this.debitCardValidator = debitCardValidator;
    }

    @ModelAttribute("debitCard")
    public DebitCard getDebitCard(@PathVariable("cardNumber") String cardNumber) {
        return debitCardService.getDebitCardByCardNumber(cardNumber)
                .orElseThrow(() -> new NoSuchElementException("Debit card with number '%s' not found".formatted(cardNumber)));
    }

    @GetMapping
    public String getDebitCardPage() {
        return "debit-cards/debit_card";
    }

    @GetMapping("/edit")
    public String getEditDebitCardPage() {
        return "debit-cards/update_debit_card";
    }

    @PutMapping("/edit")
    public String updateDebitCard(
            @ModelAttribute(value = "debitCard", binding = false) final DebitCard debitCard,
            @Valid @ModelAttribute("debitCardPayload") final UpdateDebitCardPayload payload,
            final BindingResult bindingResult,
            final Model model) {
        this.debitCardValidator.validate(payload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "debit-cards/update_debit_card";
        } else {
            final boolean isUpdate =  debitCardService
                    .updateDebitCard(payload.balance(),debitCard.getCardNumber());
            return "redirect:/debit-cards/%s".formatted(debitCard.getCardNumber());
        }
    }

}
