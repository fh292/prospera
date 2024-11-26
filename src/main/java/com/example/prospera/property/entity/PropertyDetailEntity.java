package com.example.prospera.property.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_property_details")
public class PropertyDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id")
    private PropertyEntity property;
}
