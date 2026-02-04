package com.orion.DigiWallet.controller;

import com.orion.DigiWallet.model.Card;
import com.orion.DigiWallet.service.CardService;
import org.springframework.web.bind.annotation.*;

//TODO: 4.4.1 review card controller api
// review using swagger ui
// also test using unit testing check testing class if exists

@RestController
@RequestMapping("/api/cards")
public class CardController {


    // private cardservice variable here
    // Inject CardService using constructor injection
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    @PostMapping("/create")
    public Card createCard(@RequestBody Card card) {
        return cardService.createCard(card);
    }

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @GetMapping("/{id}")
    public Card getCardById(@PathVariable Long id) {
        return cardService.getCardById(id);
    }

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @PutMapping("/{id}")
    public Card updateCard(
            @PathVariable Long id,
            @RequestBody Card updatedCard) {

        return cardService.updateCard(id, updatedCard);
    }

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @DeleteMapping("/{id}")
    public String deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return "Card deleted successfully";
    }
}
