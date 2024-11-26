package com.example.prospera.property.bo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequest {
    private String name;
    private String location;
    private Integer totalShares;
    private Integer currentValue;
    private Integer rentalIncome;
    private String typeOfProperty;
    private String latitude;
    private String longitude;
    private String locationAddress;
    private String locationName;
    private String description;
    private Integer numberOfShares;
    private Integer availableShares;
    private Integer propertyPrice;
    private Double propertySize;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;

    //private List<PropertyValueRequest> propertyValues;

}
