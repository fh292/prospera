package com.example.prospera.investment.controller;

import com.example.prospera.investment.InvestmentEntity;
import com.example.prospera.investment.InvestmentRepository;
import com.example.prospera.investment.bo.InvestmentRequest;
import com.example.prospera.investment.bo.InvestmentResponse;
import com.example.prospera.investment.service.InvestmentService;
import com.example.prospera.property.bo.PropertyRequest;
import com.example.prospera.property.bo.PropertyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investments")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    private final InvestmentRepository investmentRepository;
    public InvestmentController(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    @GetMapping("/view")
    public ResponseEntity<List<InvestmentResponse>> getAllInvestment() {
        return ResponseEntity.ok(investmentService.getAllInvestments());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<InvestmentResponse> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(investmentService.getInvestmentById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<InvestmentResponse> addInvestment(@RequestParam Long userId, @RequestParam Long propertyId, @RequestBody InvestmentRequest request) {
        InvestmentResponse investmentResponse = investmentService.addInvestment(userId, propertyId, request);
        return ResponseEntity.ok(investmentResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<InvestmentResponse> updateInvestment(@PathVariable Long id, @RequestBody InvestmentRequest investmentRequest) {
        return ResponseEntity.ok(investmentService.updateInvestment(id, investmentRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInvestmentById(@PathVariable("id") Long id) {
        try {
            investmentService.deleteInvestmentById(id);
            return ResponseEntity.ok("Investment has been successfully deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}
