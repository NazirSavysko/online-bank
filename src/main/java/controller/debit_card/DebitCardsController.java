package controller.debit_card;

import controller.payload.debit_card.NewDebitCardPayload;
import entity.DebitCard;
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
import service.DebitCardService;
import utils.MyOwnValidator;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/debit-cards")
public final class DebitCardsController {

    private final DebitCardService debitCardService;
    private final Validator debitCardValidator;

    @Autowired
    public DebitCardsController(final DebitCardService debitCardService,
                                @Qualifier("myOwnValidator") final Validator debitCardValidator) {
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
            @Valid @ModelAttribute("debitCardPayload") final NewDebitCardPayload payload,
            final BindingResult bindingResult,
            final Model model
    ) {
        this.debitCardValidator.validate(payload, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "debit-cards/new_debit_card";
        } else {
            final Optional<DebitCard> debitCard = this.debitCardService.saveDebitCard(
                    payload.cardHolderPassportNumber()
            );
            if (debitCard.isPresent()) {
                return "redirect:/debit-cards/%s".formatted(debitCard.get().getCardNumber());
            } else {
                ObjectError error = new ObjectError("debitCardPayload", "Invalid number of holder's passport");
                model.addAttribute("errors",error);
                return "debit-cards/new_debit_card";
            }
        }
    }
}
