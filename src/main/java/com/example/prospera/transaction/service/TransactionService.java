package com.example.prospera.transaction.service;

import com.example.prospera.investment.InvestmentEntity;
import com.example.prospera.investment.InvestmentRepository;
import com.example.prospera.property.PropertyRepository;
import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.transaction.TransactionEntity;
import com.example.prospera.transaction.TransactionRepository;
import com.example.prospera.transaction.bo.ShareTransactionResponse;
import com.example.prospera.transaction.bo.TransactionRequest;
import com.example.prospera.transaction.bo.TransactionResponse;
import com.example.prospera.users.UserEntity;
import com.example.prospera.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final InvestmentRepository investmentRepository;

    public List<TransactionResponse> getAllTransactions() {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction with ID " + id + " not found"));
        return new TransactionResponse(transaction);
    }

    public TransactionResponse deposit(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        UserEntity user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (transactionRequest.getAmount() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        user.setBalance(user.getBalance() + transactionRequest.getAmount());

        TransactionEntity transaction = TransactionEntity.builder()
                .type("deposit")
                .amount(transactionRequest.getAmount())
                .user(user)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        transaction = transactionRepository.save(transaction);
        user = userRepository.save(user);

        return new TransactionResponse(transaction);
    }

    public TransactionResponse withdraw(TransactionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero.");
        }

        if (user.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal.");
        }

        user.setBalance(user.getBalance() - request.getAmount());
        user = userRepository.save(user);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setType("withdraw");
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transaction = transactionRepository.save(transaction);

        return new TransactionResponse(transaction);
    }

    public ShareTransactionResponse buyShare(Long propertyId, TransactionRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        UserEntity user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        PropertyEntity property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found"));

        double sharePrice = (double) property.getPropertyPrice() / property.getTotalShares(); // Derive share price
        double totalCost = sharePrice * request.getAmount();

        if (property.getAvailableShares() < request.getAmount()) {
            throw new IllegalArgumentException("Not enough shares available for purchase.");
        }

        if (user.getBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance to buy shares.");
        }

        user.setBalance(user.getBalance() - totalCost);

        property.setAvailableShares(property.getAvailableShares() - request.getAmount());

        // Check if the user already has an investment in this property
        InvestmentEntity investment = investmentRepository.findByUserAndProperty(user, property)
                .orElseGet(() -> {
                    // If no investment exists, create a new one
                    InvestmentEntity newInvestment = new InvestmentEntity();
                    newInvestment.setUser(user);
                    newInvestment.setProperty(property);
                    newInvestment.setSharesOwned(0.0); // Set initial shares owned to 0
                    newInvestment.setAmountInvested(0.0); // Set initial invested amount to 0
                    return newInvestment;
                });

        // Update the investment details
        investment.setSharesOwned(investment.getSharesOwned() + request.getAmount());
        investment.setAmountInvested(investment.getAmountInvested() + totalCost);

        // Save user, property, and investment
        userRepository.save(user);
        propertyRepository.save(property);
        investmentRepository.save(investment);

        // Create and save the transaction
        TransactionEntity transaction = new TransactionEntity();
        transaction.setType("buy share");
        transaction.setUser(user);
        // transaction.setProperty(property);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transactionRepository.save(transaction);

        // Return the response with transaction details and investment status
        return new ShareTransactionResponse(transaction, investment);
    }

    public ShareTransactionResponse sellShare(Long propertyId, TransactionRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

        UserEntity user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        PropertyEntity property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found"));

        double sharePrice = (double) property.getPropertyPrice() / property.getTotalShares(); // Derive share price
        double totalCost = sharePrice * request.getAmount();

        InvestmentEntity investment = investmentRepository.findByUserAndProperty(user, property)
                .orElseThrow(() -> new IllegalArgumentException("No investment found for user in property"));

        if (investment.getSharesOwned() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient shares owned to sell.");
        }

        investment.setSharesOwned(investment.getSharesOwned() - request.getAmount());
        investment.setAmountInvested(investment.getAmountInvested() - totalCost);

        user.setBalance(user.getBalance() + totalCost);

        property.setAvailableShares(property.getAvailableShares() + request.getAmount());

        userRepository.save(user);
        propertyRepository.save(property);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setType("sell share");
        transaction.setUser(user);
        // transaction.setProperty(property);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transactionRepository.save(transaction);

        if (investment.getSharesOwned() == 0) {
            investmentRepository.delete(investment);
            return new ShareTransactionResponse(transaction, null);
        } else {
            investmentRepository.save(investment);
            return new ShareTransactionResponse(transaction, investment);
        }

    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest transactionRequest) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction with ID " + id + " not found"));

        if (transactionRequest.getType() != null) {
            transaction.setType(transactionRequest.getType());
        }
        if (transactionRequest.getAmount() != null) {
            transaction.setAmount(transactionRequest.getAmount());
        }

        transaction.setUpdatedAt(new Date());

        TransactionEntity updatedTransaction = transactionRepository.save(transaction);
        return new TransactionResponse(updatedTransaction);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction with ID " + id + " not found");
        }
        transactionRepository.deleteById(id);
    }

}
