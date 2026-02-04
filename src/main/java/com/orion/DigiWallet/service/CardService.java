package com.orion.DigiWallet.service;

import com.orion.DigiWallet.model.Card;
import com.orion.DigiWallet.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card createCard(Card card) {

        if (cardRepository.existsByCardNumber(card.getCardNumber())) {
            throw new RuntimeException("Card number already exists");
        }

        return cardRepository.save(card);
    }

    public Card getCardById(Long id) {

        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));
    }

    public Card updateCard(Long id, Card updatedCard) {

        Card existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));

        existingCard.setWallet(updatedCard.getWallet());
        existingCard.setCardType(updatedCard.getCardType());
        existingCard.setExpiryDate(updatedCard.getExpiryDate());
        existingCard.setStatus(updatedCard.getStatus());

        return cardRepository.save(existingCard);
    }

    public void deleteCard(Long id) {

        Card existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));

        cardRepository.delete(existingCard);

    }
}
