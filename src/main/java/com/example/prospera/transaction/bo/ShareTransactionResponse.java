package com.example.prospera.transaction.bo;

import com.example.prospera.investment.InvestmentEntity;
import com.example.prospera.transaction.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ShareTransactionResponse {
    private UUID id;
    private Long userId;
    private String type;
    private Double amount;
    private Date createdAt;
    private Date updatedAt;
    private Double investmentSharesOwned;
    private Double investmentAmountInvested;

    public ShareTransactionResponse(TransactionEntity transactionEntity, InvestmentEntity investment) {
        if (investment == null) {
            return; // investment is deleted from database bc user sold all shares
        }
        this.id = transactionEntity.getId();
        this.userId = transactionEntity.getUser() != null ? transactionEntity.getUser().getId() : null;
        this.type = transactionEntity.getType();
        this.amount = transactionEntity.getAmount();
        this.createdAt = transactionEntity.getCreatedAt();
        this.updatedAt = transactionEntity.getUpdatedAt();
        this.investmentSharesOwned = investment.getSharesOwned();
        this.investmentAmountInvested = investment.getAmountInvested();
    }
}
