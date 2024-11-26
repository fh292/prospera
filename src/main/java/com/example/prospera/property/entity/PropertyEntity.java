package com.example.prospera.property.entity;
import com.example.prospera.investment.InvestmentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_property")
public class PropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "totalShares")
    private Integer totalShares;

    @Column(name = "availableShares")
    private Integer availableShares;

    @Column(name = "rentalIncome")
    private Integer rentalIncome;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "currentValue")
    private Integer currentValue;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PropertyValueEntity> propertyValues = new ArrayList<>();

    public void addToPropertyValues(PropertyValueEntity propertyValueEntity) {
        propertyValues.add(propertyValueEntity);
    }

    @Column(name = "type_of_property")
    private String typeOfProperty;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_shares")
    private Integer numberOfShares;

    @Column(name = "property_price")
    private Integer propertyPrice;

    @Column(name = "property_size")
    private Double propertySize;

    @Column(name = "number_of_bedrooms")
    private Integer numberOfBedrooms;

    @Column(name = "number_of_bathrooms")
    private Integer numberOfBathrooms;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<InvestmentEntity> investments = new ArrayList<>();

    public void addInvestment(InvestmentEntity investment) {
        investments.add(investment);
        investment.setProperty(this);
    }

    public void removeInvestment(InvestmentEntity investment) {
        investments.remove(investment);
        investment.setProperty(null);
    }

}
