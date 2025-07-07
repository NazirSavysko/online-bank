package controller.debit_card;

import controller.payload.debit_card.NewDebitCardPayload;
import controller.payload.debit_card.TransferPayload;
import dto.DebitCardDTO;
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

import javax.validation.Valid;

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
    public String createDebitCard(@Valid @ModelAttribute("debitCardPayload") final NewDebitCardPayload payload,
                                  final BindingResult bindingResult,
                                  final Model model) {
        this.debitCardValidator.validate(payload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "debit-cards/new_debit_card";
        } else {
            try {
                final DebitCardDTO debitCard = this.debitCardService.saveDebitCard(
                        payload.cardHolderPassportNumber(),
                        payload.cardNumber(),
                        payload.cvv(),
                        payload.balance(),
                        payload.expirationDate(),
                        payload.issueDate()
                );

                return "redirect:/debit-cards/%d".formatted(debitCard.id());
            } catch (final IllegalArgumentException e) {
                ObjectError error = new ObjectError("debitCardPayload", e.getMessage());
                model.addAttribute("errors", error);

                return "debit-cards/new_debit_card";
            }
        }
    }

    @GetMapping("/transfer")
    public String getTransferPage() {
        return "debit-cards/transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(
            @ModelAttribute("transferPayload") final TransferPayload transferPayload,
            final BindingResult bindingResult,
            final Model model) {
        debitCardValidator.validate(transferPayload, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());

            return "debit-cards/transfer";
        }else {
            try {
                debitCardService.transferMoney(
                        transferPayload.fromCardNumber(),
                        transferPayload.toCardNumber(),
                        transferPayload.amount()
                );

                return "redirect:/debit-cards/list";
            } catch (IllegalArgumentException e) {
                ObjectError error = new ObjectError("transferPayload", e.getMessage());
                model.addAttribute("errors", error);

                return "debit-cards/transfer";
            }
        }
    }
}
