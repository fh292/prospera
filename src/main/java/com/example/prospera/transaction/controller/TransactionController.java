package com.example.prospera.transaction.controller;

import com.example.prospera.transaction.TransactionEntity;
import com.example.prospera.transaction.bo.ShareTransactionResponse;
import com.example.prospera.transaction.bo.TransactionRequest;
import com.example.prospera.transaction.bo.TransactionResponse;
import com.example.prospera.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/view")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.deposit(request);
        return ResponseEntity.ok(transactionResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.withdraw(request);
        return ResponseEntity.ok(transactionResponse);
    }

    @PostMapping("/buy-share")
    public ResponseEntity<ShareTransactionResponse> buyShare(@RequestParam Long propertyId, @RequestBody TransactionRequest request) {

        ShareTransactionResponse transactionResponse = transactionService.buyShare(propertyId, request);
        return ResponseEntity.ok(transactionResponse);
    }

    @PostMapping("/sell-share")
    public ResponseEntity<ShareTransactionResponse> sellShare(@RequestParam Long propertyId, @RequestBody TransactionRequest request) {
        ShareTransactionResponse transactionResponse = transactionService.sellShare(propertyId, request);
        return ResponseEntity.ok(transactionResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction has been successfully deleted.");
    }
}
