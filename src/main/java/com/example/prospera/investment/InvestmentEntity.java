package com.example.prospera.investment;

import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.users.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_investment")
public class InvestmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double sharesOwned;
    private Double amountInvested;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = { "investments" })
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @JsonIgnoreProperties(value = { "investments" })
    private PropertyEntity property;

    @Column(name = "investment_created_at")
    private Date createdAt;

    @Column(name = "investment_updated_at")
    private Date updatedAt;

}
