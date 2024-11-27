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

    private String locationName;
    private String locationAddress;
    private String locationCity;
    private String latitude;
    private String longitude;
    private String description;
    private String typeOfProperty;
    private Double propertySize;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private Integer totalShares;
    private Double availableShares;
    private Integer rentalIncome;
    private Integer currentValue;
    private List<String> imagesUrls;

    private List<PropertyValueResponse> propertyValues;

    public PropertyResponse(PropertyEntity entity) {
        this.id = entity.getId();
        this.totalShares = entity.getTotalShares();
        this.availableShares = entity.getAvailableShares();
        this.rentalIncome = entity.getRentalIncome();
        this.currentValue = entity.getCurrentValue();
        this.typeOfProperty = entity.getTypeOfProperty();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
        this.locationAddress = entity.getLocationAddress();
        this.locationName = entity.getLocationName();
        this.locationCity = entity.getLocationCity();
        this.description = entity.getDescription();
        this.propertySize = entity.getPropertySize();
        this.numberOfBedrooms = entity.getNumberOfBedrooms();
        this.numberOfBathrooms = entity.getNumberOfBathrooms();
        this.imagesUrls = entity.getImagesUrls().stream()
                .map(String::new)
                .collect(Collectors.toList());
        this.propertyValues = entity.getPropertyValues().stream()
                .map(PropertyValueResponse::new)
                .collect(Collectors.toList());
    }

}
