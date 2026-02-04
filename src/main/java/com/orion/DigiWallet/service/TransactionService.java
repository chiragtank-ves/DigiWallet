package com.orion.DigiWallet.service;

import com.orion.DigiWallet.model.Category;
import com.orion.DigiWallet.model.Transaction;
import com.orion.DigiWallet.model.Wallet;
import com.orion.DigiWallet.repository.CategoryRepository;
import com.orion.DigiWallet.repository.TransactionRepository;
import com.orion.DigiWallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
public class TransactionService {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionService.class);

    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(
            WalletRepository walletRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository) {

        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    //UNCOMMENT TO ENABLE CREATE TRANSACTION FUNCTIONALITY
    // --------------------------------------------------
    @Transactional
    public Transaction createTransaction(
            Long walletId,
            Long categoryId,
            Double amount) {
        // 1. Fetch Wallet
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // 2. Fetch Category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 3. Validate Amount
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        BigDecimal txnAmount = BigDecimal.valueOf(amount);
        BigDecimal currentBalance = wallet.getBalance();

        String transactionType;

        // 4. Decide CREDIT or DEBIT based on Category Type
        if ("INCOME".equalsIgnoreCase(category.getType())) {

            // CREDIT → Add money
            wallet.setBalance(currentBalance.add(txnAmount));
            transactionType = "CREDIT";

        } else if ("EXPENSE".equalsIgnoreCase(category.getType())) {

            // DEBIT → Check balance first
            if (currentBalance.compareTo(txnAmount) < 0) {
                throw new RuntimeException("Insufficient wallet balance");
            }

            wallet.setBalance(currentBalance.subtract(txnAmount));
            transactionType = "DEBIT";

        } else {
            throw new RuntimeException("Invalid category type");
        }

        // 5. Save Updated Wallet
        walletRepository.save(wallet);

        // 6. Create Transaction Record
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setReferenceId(generateReferenceId());

        return transactionRepository.save(transaction);
    }

    // Utility method
    private String generateReferenceId() {
        return "TXN-" + System.currentTimeMillis();
    }

    // --------------------------------------------------
    // GET TRANSACTION BY ID
    // --------------------------------------------------
    public Transaction getTransactionById(Long id) {
        logger.info("Fetching transaction with id {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    // --------------------------------------------------
    // GET TRANSACTIONS BY USER ID
    // --------------------------------------------------
    public List<Transaction> getTransactionsByUserId(Long userId) {
        logger.info("Fetching transactions for userId {}", userId);
        return transactionRepository.findByWallet_User_Id(userId);
    }
}
