package controller.debit_card;

import controller.payload.debit_card.UpdateDebitCardPayload;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import service.DebitCardService;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/debit-cards/{cardId:\\d+}")
public final class DebitCardController {

    private final DebitCardService debitCardService;
    private final Validator debitCardValidator;

    @Autowired
    public DebitCardController(final DebitCardService debitCardService,
                               @Qualifier("myOwnValidator") final Validator debitCardValidator) {
        this.debitCardService = debitCardService;
        this.debitCardValidator = debitCardValidator;
    }

    @ModelAttribute("debitCard")
    public DebitCard getDebitCard(@PathVariable("cardId") int id) {
        return debitCardService.getDebitCardById(id)
                .orElseThrow(() -> new NoSuchElementException("Debit card with number '%d' not found".formatted(id)));
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
            try {
                debitCardService.updateDebitCard(
                        debitCard.getId(),
                        payload.cardHolderPassportNumber(),
                        payload.cardNumber(),
                        payload.cvv(),
                        payload.balance(),
                        payload.expirationDate(),
                        payload.issueDate()
                );

                return "redirect:/debit-cards/%s".formatted(debitCard.getCardNumber());
            } catch (IllegalArgumentException e) {
                ObjectError error = new ObjectError("debitCardPayload", e.getMessage());
                model.addAttribute("errors", error);
                return "debit-cards/update_debit_card";
            }
        }
    }

    @DeleteMapping("/delete")
    public String deleteDebitCard(@ModelAttribute(value = "debitCard", binding = false) final DebitCard debitCard) {
        debitCardService.deleteDebitCard(debitCard.getId());
        return "redirect:/debit-cards/list";
    }
}
