package com.example.prospera.transaction.bo;
import com.example.prospera.investment.InvestmentEntity;
import com.example.prospera.transaction.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Long id;
    private Long userId;
    private Long propertyId;
    private String type;
    private Double amount;
    private Date createdAt;
    private Date updatedAt;

    public TransactionResponse(TransactionEntity transactionEntity) {
        this.id = transactionEntity.getId();
        this.userId = transactionEntity.getUser() != null ? transactionEntity.getUser().getId() : null;
        this.propertyId = transactionEntity.getProperty() != null ? transactionEntity.getProperty().getId() : null;
        this.type = transactionEntity.getType();
        this.amount = transactionEntity.getAmount();
        this.createdAt = transactionEntity.getCreatedAt();
        this.updatedAt = transactionEntity.getUpdatedAt();

    }
}
