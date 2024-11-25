package com.example.prospera.investment.bo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentRequest {

    private String name;
    private Long sharesOwned;
    private Double amountInvested;


}
