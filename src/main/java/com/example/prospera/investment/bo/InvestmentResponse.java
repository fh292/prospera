package com.example.prospera.investment.bo;

import com.example.prospera.investment.InvestmentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class InvestmentResponse {
    private Long id;
    private Long userId;
    private Long propertyId;
    private String name;
    private Double sharesOwned;
    private Double amountInvested;
    private Date createdAt;
    private Date updatedAt;

    public InvestmentResponse(InvestmentEntity investmentEntity) {
        this.id = investmentEntity.getId();
        this.userId = investmentEntity.getUser().getId();
        this.propertyId = investmentEntity.getProperty().getId();
        this.name = investmentEntity.getName();
        this.sharesOwned = investmentEntity.getSharesOwned();
        this.amountInvested = investmentEntity.getAmountInvested();
        this.createdAt = investmentEntity.getCreatedAt();
        this.updatedAt = investmentEntity.getUpdatedAt();
    }
}
