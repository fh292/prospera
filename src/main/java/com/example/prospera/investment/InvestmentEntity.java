package com.example.prospera.investment;
import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.users.UserEntity;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity property;

    private String name;
    private Long sharesOwned;
    private Double amountInvested;
    private Date createdAt;
    private Date updatedAt;


}
