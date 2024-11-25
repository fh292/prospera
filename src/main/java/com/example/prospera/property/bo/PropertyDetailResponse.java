package com.example.prospera.property.bo;
import com.example.prospera.property.entity.PropertyDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDetailResponse {
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

    public PropertyDetailResponse(PropertyDetailEntity detail) {
        this.latitude = detail.getLatitude();
        this.longitude = detail.getLongitude();
        this.locationAddress = detail.getLocationAddress();
        this.locationName = detail.getLocationName();
        this.description = detail.getDescription();
        this.numberOfShares = detail.getNumberOfShares();
        this.propertyPrice = detail.getPropertyPrice();
        this.propertySize = detail.getPropertySize();
        this.numberOfBedrooms = detail.getNumberOfBedrooms();
        this.numberOfBathrooms = detail.getNumberOfBathrooms();
    }
}
