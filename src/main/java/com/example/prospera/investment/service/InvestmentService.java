package com.example.prospera.investment.service;

import com.example.prospera.investment.InvestmentEntity;
import com.example.prospera.investment.InvestmentRepository;
import com.example.prospera.investment.bo.InvestmentRequest;
import com.example.prospera.investment.bo.InvestmentResponse;
import com.example.prospera.property.PropertyRepository;
import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.users.UserEntity;
import com.example.prospera.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    // get all investments
    public List<InvestmentResponse> getAllInvestments() {
        List<InvestmentEntity> investmentEntities = investmentRepository.findAll();
        return investmentEntities.stream()
                .map(InvestmentResponse::new)
                .collect(Collectors.toList());
    }

    // get investment by id
    public InvestmentResponse getInvestmentById(Long id) {
        InvestmentEntity investmentEntity = investmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property with ID " + id + " not found"));
        InvestmentResponse response = new InvestmentResponse(investmentEntity);
        return response;
    }

    // add an investment
    public InvestmentResponse addInvestment(Long userId, Long propertyId, InvestmentRequest investmentRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        PropertyEntity property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property with ID " + propertyId + " not found"));

        InvestmentEntity investmentEntity = InvestmentEntity.builder()
                .user(user)
                .property(property)
                .sharesOwned(investmentRequest.getSharesOwned())
                .amountInvested(investmentRequest.getAmountInvested())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        InvestmentEntity savedInvestmentEntity = investmentRepository.save(investmentEntity);
        InvestmentResponse addedResponse = new InvestmentResponse(savedInvestmentEntity);
        return addedResponse;
    }

    // update an investment
    public InvestmentResponse updateInvestment(Long id, InvestmentRequest investmentRequest) {
        InvestmentEntity investment = investmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Investment with ID " + id + " not found"));

        if (investmentRequest.getSharesOwned() != null) {
            investment.setSharesOwned(investmentRequest.getSharesOwned());
        }
        if (investmentRequest.getAmountInvested() != null) {
            investment.setAmountInvested(investmentRequest.getAmountInvested());
        }
        investment.setUpdatedAt(new Date());
        InvestmentEntity updatedResponse = investmentRepository.save(investment);
        return new InvestmentResponse(updatedResponse);
    }

    // delete an investment
    public void deleteInvestmentById(Long id) {
        if (!investmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Investment with ID " + id + " not found");
        }
        investmentRepository.deleteById(id);
    }
}
