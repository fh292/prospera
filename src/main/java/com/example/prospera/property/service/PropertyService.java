package com.example.prospera.property.service;
import com.example.prospera.property.PropertyRepository;
import com.example.prospera.property.bo.PropertyRequest;
import com.example.prospera.property.bo.PropertyResponse;
import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.property.entity.PropertyValueEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;

    //get all properties
    public List<PropertyResponse> getAllProperties() {
        List<PropertyEntity> propertyEntities = propertyRepository.findAll();
        return propertyEntities.stream()
                .map(PropertyResponse::new)
                .collect(Collectors.toList());
    }

    //get property by id
    public PropertyResponse getPropertyById(Long id) {
        PropertyEntity propertyEntity = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property with ID " + id + " not found"));
        PropertyResponse responseId = new PropertyResponse(propertyEntity);
        return responseId;
    }


    //adding new property
    public PropertyResponse addProperty(PropertyRequest propertyRequest) {
        PropertyEntity propertyEntity = new PropertyEntity();

        propertyEntity.setName(propertyRequest.getName());
        propertyEntity.setLocation(propertyRequest.getLocation());
        propertyEntity.setTotalShares(propertyRequest.getTotalShares());
        propertyEntity.setAvailableShares(propertyRequest.getAvailableShares());
        propertyEntity.setRentalIncome(propertyRequest.getRentalIncome());
        propertyEntity.setCurrentValue(propertyRequest.getCurrentValue());
        propertyEntity.setLatitude(propertyRequest.getLatitude());
        propertyEntity.setLongitude(propertyRequest.getLongitude());
        propertyEntity.setLocationAddress(propertyRequest.getLocationAddress());
        propertyEntity.setLocationName(propertyRequest.getLocationName());
        propertyEntity.setDescription(propertyRequest.getDescription());
        propertyEntity.setNumberOfShares(propertyRequest.getNumberOfShares());
        propertyEntity.setPropertyPrice(propertyRequest.getPropertyPrice());
        propertyEntity.setPropertySize(propertyRequest.getPropertySize());
        propertyEntity.setNumberOfBedrooms(propertyRequest.getNumberOfBedrooms());
        propertyEntity.setNumberOfBathrooms(propertyRequest.getNumberOfBathrooms());

        propertyEntity.setCreatedAt(new Date());
        propertyEntity.setUpdatedAt(new Date());

        PropertyValueEntity propertyValueEntity = new PropertyValueEntity();
        propertyValueEntity.setPropertyValue(propertyRequest.getCurrentValue());
        propertyValueEntity.setAvailableShares(propertyRequest.getAvailableShares());
        propertyValueEntity.setValueDate(new Date());
        propertyValueEntity.setProperty(propertyEntity);
        propertyEntity.addToPropertyValues(propertyValueEntity);


        propertyEntity = propertyRepository.save(propertyEntity);
        return new PropertyResponse(propertyEntity);
    }

    //update a property
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Optional<PropertyEntity> propertyOptional = propertyRepository.findById(id);
        if (propertyOptional.isPresent()) {
            PropertyEntity propertyEntity = propertyOptional.get();

            if (request.getName() != null) {
                propertyEntity.setName(request.getName());
            }
            if (request.getLocation() != null) {
                propertyEntity.setLocation(request.getLocation());
            }
            if (request.getTotalShares() != null) {
                propertyEntity.setTotalShares(request.getTotalShares());
            }
            if (request.getRentalIncome() != null) {
                propertyEntity.setRentalIncome(request.getRentalIncome());
            }
            Boolean currentValueChanged = request.getCurrentValue() != null && !request.getCurrentValue().equals(propertyEntity.getCurrentValue());
            Boolean currentAvailableSharesChanged = request.getAvailableShares() != null && !request.getAvailableShares().equals(propertyEntity.getAvailableShares());
            if (currentValueChanged || currentAvailableSharesChanged) {
                if (currentValueChanged) {
                    propertyEntity.setCurrentValue(request.getCurrentValue());
                }
                if (currentAvailableSharesChanged) {
                    propertyEntity.setAvailableShares(request.getAvailableShares());
                }

                PropertyValueEntity propertyValueEntity = new PropertyValueEntity();
                propertyValueEntity.setPropertyValue(propertyEntity.getCurrentValue());
                propertyValueEntity.setAvailableShares(propertyEntity.getAvailableShares());
                propertyValueEntity.setValueDate(new Date());
                propertyValueEntity.setProperty(propertyEntity);

                propertyEntity.addToPropertyValues(propertyValueEntity);
            }
            propertyEntity.setUpdatedAt(new Date());

            if (request.getLatitude() != null) {
                propertyEntity.setLatitude(request.getLatitude());
            }
            if (request.getLongitude() != null) {
                propertyEntity.setLongitude(request.getLongitude());
            }
            if (request.getLocationAddress() != null) {
                propertyEntity.setLocationAddress(request.getLocationAddress());
            }
            if (request.getLocationName() != null) {
                propertyEntity.setLocationName(request.getLocationName());
            }
            if (request.getDescription() != null) {
                propertyEntity.setDescription(request.getDescription());
            }
            if (request.getNumberOfShares() != null) {
                propertyEntity.setNumberOfShares(request.getNumberOfShares());
            }
            if (request.getPropertyPrice() != null) {
                propertyEntity.setPropertyPrice(request.getPropertyPrice());
            }
            if (request.getPropertySize() != null) {
                propertyEntity.setPropertySize(request.getPropertySize());
            }
            if (request.getNumberOfBedrooms() != null) {
                propertyEntity.setNumberOfBedrooms(request.getNumberOfBedrooms());
            }
            if (request.getNumberOfBathrooms() != null) {
                propertyEntity.setNumberOfBathrooms(request.getNumberOfBathrooms());
            }

            propertyEntity = propertyRepository.save(propertyEntity);

            PropertyResponse propertyResponse = new PropertyResponse(propertyEntity);
            return propertyResponse;
        } else {
            throw new IllegalArgumentException("Property with ID " + id + " not found");
        }

    }

    public void deletePropertyById(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property with ID " + id + " not found");
        }
        propertyRepository.deleteById(id);
    }

}
