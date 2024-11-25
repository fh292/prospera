package com.example.prospera.property.bo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDetailRequest {
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
}
