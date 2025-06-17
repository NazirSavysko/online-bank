package controller.debit_card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.DebitCardService;

@Controller
@RequestMapping("/debit-cards")
public final class DebitCardsController {

    private final DebitCardService debitCardService;

    @Autowired
    public DebitCardsController(final DebitCardService debitCardService) {
        this.debitCardService = debitCardService;
    }

    @GetMapping("list")
    public String getDebitCardsPage(final Model model) {
        model.addAttribute("debitCards",this.debitCardService.getAllDebitCards());
        return "debit-cards/list";
    }

}
