package com.example.prospera.transaction;

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
@Table(name = "_transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"transactions"})
    private UserEntity user;


    @Column(name = "type")
    private String type; // deposit, withdraw, buy share, sell share

    @Column(name = "amount")
    private Double amount;

    @Column(name = "trans_created_at")
    private Date createdAt;

    @Column(name = "trans_updated_at")
    private Date updatedAt;


}
