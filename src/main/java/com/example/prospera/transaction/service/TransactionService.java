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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public TransactionResponse deposit(Long userId, TransactionRequest transactionRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

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

        transactionRepository.save(transaction);
        userRepository.save(user);

        return new TransactionResponse(transaction);
    }

    public TransactionResponse withdraw(Long userId, TransactionRequest request) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero.");
        }

        if (user.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal.");
        }

        user.setBalance(user.getBalance() - request.getAmount());
        userRepository.save(user);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setType("withdraw");
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transactionRepository.save(transaction);

        return new TransactionResponse(transaction);
    }


    public ShareTransactionResponse buyShare(Long userId, Long propertyId, TransactionRequest request) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
        PropertyEntity property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found"));

        double sharePrice = (double) property.getPropertyPrice() / property.getTotalShares(); // Derive share price
        double totalCost = sharePrice * request.getAmount();

        if (user.getBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance to buy shares.");
        }

        if (property.getAvailableShares() < request.getAmount()) {
            throw new IllegalArgumentException("Not enough shares available for purchase.");
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
        transaction.setProperty(property);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transactionRepository.save(transaction);

        // Return the response with transaction details and investment status
        return new ShareTransactionResponse(transaction, investment);
    }

//    public ShareTransactionResponse sellShare(Long userId, Long propertyId, TransactionRequest request) {
//
//        // Fetch the user and property entities
//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
//        PropertyEntity property = propertyRepository.findById(propertyId)
//                .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found"));
//
//        // Fetch all investments the user has in this property
//        Optional<InvestmentEntity> investments = investmentRepository.findByUserAndProperty(user, property);
//
//        // Calculate the total shares the user owns in this property
//        double totalSharesOwned = investments.stream().mapToDouble(InvestmentEntity::getSharesOwned).sum();
//
//        // Check if the user has enough shares to sell
//        if (totalSharesOwned < request.getAmount()) {
//            throw new IllegalArgumentException("Insufficient shares to sell.");
//        }
//
//        // Calculate share price
//        double sharePrice = (double) property.getPropertyPrice() / property.getTotalShares();
//        double totalRevenue = sharePrice * request.getAmount();
//
//        // Update user's balance
//        user.setBalance(user.getBalance() + totalRevenue);
//
//        // Deduct the shares from investments
//        double sharesToSell = request.getAmount();
//        for (InvestmentEntity investment : investments) {
//            if (sharesToSell <= 0) break;
//
//            double investmentShares = investment.getSharesOwned();
//            double investmentAmountInvested = investment.getAmountInvested();
//
//            // If the investment has fewer shares than or equal to the shares left to sell, reduce it fully
//            if (investmentShares <= sharesToSell) {
//                sharesToSell -= investmentShares;
//                investment.setSharesOwned(0.0); // All shares in this investment are sold
//                double updatedInvestment = investmentAmountInvested - (sharePrice * investmentShares);
//                investment.setAmountInvested(Math.max(updatedInvestment, 0)); // Ensure amount does not go negative
//            } else {
//                // If this investment has more shares than the amount left to sell, deduct only the required amount
//                investment.setSharesOwned(investmentShares - sharesToSell);
//                double updatedInvestment = investmentAmountInvested - (sharePrice * sharesToSell);
//                investment.setAmountInvested(Math.max(updatedInvestment, 0));
//                sharesToSell = 0; // All shares sold
//            }
//            investmentRepository.save(investment); // Save each investment after modification
//        }
//
//        // Update property availability
//        property.setAvailableShares(property.getAvailableShares() + request.getAmount());
//
//        // Save updated user and property data
//        userRepository.save(user);
//        propertyRepository.save(property);
//
//        // Create a transaction entry
//        TransactionEntity transaction = new TransactionEntity();
//        transaction.setType("sell share");
//        transaction.setUser(user);
//        transaction.setProperty(property);
//        transaction.setAmount(request.getAmount());
//        transaction.setCreatedAt(new Date());
//        transaction.setUpdatedAt(new Date());
//        transactionRepository.save(transaction);
//
//        // Return response with transaction and investment information
//        // Assuming the first investment to return in response
//        return new ShareTransactionResponse(transaction, investments);
//    }







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
