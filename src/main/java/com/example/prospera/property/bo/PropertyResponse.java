package com.example.prospera.property.bo;
import com.example.prospera.property.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {

    private Long id;
    private String name;
    private String location;
    private Integer totalShares;
    private Integer availableShares;
    private Integer rentalIncome;
    private Integer currentValue;
    private List<PropertyValueResponse> propertyValues;
    private String latitude;
    private String longitude;
    private String locationAddress;
    private String locationName;
    private String description;
    private Integer numberOfShares;
    private Integer propertyPrice;
    private Double propertySize;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;


    public PropertyResponse(PropertyEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.location = entity.getLocation();
        this.totalShares = entity.getTotalShares();
        this.availableShares = entity.getAvailableShares();
        this.rentalIncome = entity.getRentalIncome();
        this.currentValue = entity.getCurrentValue();
        this.propertyValues = entity.getPropertyValues().stream()
                .map(PropertyValueResponse::new)
                .collect(Collectors.toList());
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
        this.locationAddress = entity.getLocationAddress();
        this.locationName = entity.getLocationName();
        this.description = entity.getDescription();
        this.numberOfShares = entity.getNumberOfShares();
        this.propertyPrice = entity.getPropertyPrice();
        this.propertySize = entity.getPropertySize();
        this.numberOfBedrooms = entity.getNumberOfBedrooms();
        this.numberOfBathrooms = entity.getNumberOfBathrooms();

    }


}
