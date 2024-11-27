package com.example.prospera.property.bo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequest {
    private String locationName;
    private String locationAddress;
    private String locationCity;
    private String description;
    private Integer totalShares;
    private Double availableShares;
    private Integer rentalIncome;
    private Integer currentValue;
    private String typeOfProperty;
    private String latitude;
    private String longitude;
    private Double propertySize;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private String imagesUrl1;
    private String imagesUrl2;
    private String imagesUrl3;
    private String imagesUrl4;
    private String imagesUrl5;
}
