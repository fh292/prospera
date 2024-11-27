package com.example.prospera.property.entity;

import com.example.prospera.property.image.ImageEntity;
import com.example.prospera.investment.InvestmentEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "location_city")
    private String locationCity;

    @Column(name = "description")
    private String description;

    @Column(name = "totalShares")
    private Integer totalShares;

    @Column(name = "availableShares")
    private Double availableShares;

    @Column(name = "rentalIncome")
    private Integer rentalIncome;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "currentValue")
    private Integer currentValue;

    @Column(name = "type_of_property")
    private String typeOfProperty;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "property_size")
    private Double propertySize;

    @Column(name = "number_of_bedrooms")
    private Integer numberOfBedrooms;

    @Column(name = "number_of_bathrooms")
    private Integer numberOfBathrooms;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "property" })
    private final List<PropertyValueEntity> propertyValues = new ArrayList<>();

    @OneToMany(mappedBy = "property")
    @JsonIgnoreProperties(value = { "property" })
    private final List<InvestmentEntity> investments = new ArrayList<>();

    public void addToPropertyValues(PropertyValueEntity propertyValueEntity) {
        propertyValues.add(propertyValueEntity);
    }

    public void addInvestment(InvestmentEntity investment) {
        investments.add(investment);
        investment.setProperty(this);
    }

    public void removeInvestment(InvestmentEntity investment) {
        investments.remove(investment);
        investment.setProperty(null);
    }

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    public void addImage(ImageEntity image) {
        images.add(image);
        image.setProperty(this);
    }

    public void removeImage(ImageEntity image) {
        images.remove(image);
        image.setProperty(null);
    }



}
