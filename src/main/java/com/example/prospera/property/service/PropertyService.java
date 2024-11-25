package com.example.prospera.property.service;
import com.example.prospera.property.PropertyRepository;
import com.example.prospera.property.bo.PropertyDetailRequest;
import com.example.prospera.property.bo.PropertyRequest;
import com.example.prospera.property.bo.PropertyResponse;
import com.example.prospera.property.entity.PropertyDetailEntity;
import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.property.entity.PropertyValueEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

//        if (propertyRequest.getPropertyDetails() != null) {
//            List<PropertyDetailEntity> details = new ArrayList<>();
//            for (PropertyDetailRequest detailRequest : propertyRequest.getPropertyDetails()) {
//                PropertyDetailEntity detailEntity = new PropertyDetailEntity();
//                detailEntity.setLatitude(detailRequest.getLatitude());
//                detailEntity.setLongitude(detailRequest.getLongitude());
//                detailEntity.setLocationAddress(detailRequest.getLocationAddress());
//                detailEntity.setLocationName(detailRequest.getLocationName());
//                detailEntity.setDescription(detailRequest.getDescription());
//                detailEntity.setNumberOfShares(detailRequest.getNumberOfShares());
//                detailEntity.setPropertyPrice(detailRequest.getPropertyPrice());
//                detailEntity.setPropertySize(detailRequest.getPropertySize());
//                detailEntity.setNumberOfBedrooms(detailRequest.getNumberOfBedrooms());
//                detailEntity.setNumberOfBathrooms(detailRequest.getNumberOfBathrooms());
//                detailEntity.setProperty(propertyEntity);
//                details.add(detailEntity);
//            }
//            propertyEntity.setPropertyDetails(details);
//        }

        propertyEntity = propertyRepository.save(propertyEntity);
        return new PropertyResponse(propertyEntity);
    }

    //update a property
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Optional<PropertyEntity> propertyOptional = propertyRepository.findById(id);
        //.orElseThrow(() -> new RuntimeException("Property with ID " + id + " not found"));
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

//            if (request.getPropertyDetails() != null) {
//                List<PropertyDetailEntity> existingDetails = propertyEntity.getPropertyDetails();
//
//                for (PropertyDetailRequest detailRequest : request.getPropertyDetails()) {
//                    // Find matching detail by latitude and longitude
//                    PropertyDetailEntity matchingDetail = existingDetails.stream()
//                            .filter(detail ->
//                                    detailRequest.getLatitude() != null && detailRequest.getLatitude().equals(detail.getLatitude()) ||
//                                            detailRequest.getLongitude() != null && detailRequest.getLongitude().equals(detail.getLongitude())
//                            )
//                            .findFirst()
//                            .orElse(null);
//
//                    if (matchingDetail != null) {
//                        // Update existing detail
//                        if (detailRequest.getLongitude() != null) matchingDetail.setLongitude(detailRequest.getLongitude());
//                        if (detailRequest.getLocationAddress() != null) matchingDetail.setLocationAddress(detailRequest.getLocationAddress());
//                        if (detailRequest.getLocationName() != null) matchingDetail.setLocationName(detailRequest.getLocationName());
//                        if (detailRequest.getDescription() != null) matchingDetail.setDescription(detailRequest.getDescription());
//                        if (detailRequest.getNumberOfShares() != null) matchingDetail.setNumberOfShares(detailRequest.getNumberOfShares());
//                        if (detailRequest.getPropertyPrice() != null) matchingDetail.setPropertyPrice(detailRequest.getPropertyPrice());
//                        if (detailRequest.getPropertySize() != null) matchingDetail.setPropertySize(detailRequest.getPropertySize());
//                        if (detailRequest.getNumberOfBedrooms() != null) matchingDetail.setNumberOfBedrooms(detailRequest.getNumberOfBedrooms());
//                        if (detailRequest.getNumberOfBathrooms() != null) matchingDetail.setNumberOfBathrooms(detailRequest.getNumberOfBathrooms());
//                    } else {
//                        // Add new detail
//                        PropertyDetailEntity newDetail = new PropertyDetailEntity();
//                        newDetail.setLatitude(detailRequest.getLatitude());
//                        newDetail.setLongitude(detailRequest.getLongitude());
//                        newDetail.setLocationAddress(detailRequest.getLocationAddress());
//                        newDetail.setLocationName(detailRequest.getLocationName());
//                        newDetail.setDescription(detailRequest.getDescription());
//                        newDetail.setNumberOfShares(detailRequest.getNumberOfShares());
//                        newDetail.setPropertyPrice(detailRequest.getPropertyPrice());
//                        newDetail.setPropertySize(detailRequest.getPropertySize());
//                        newDetail.setNumberOfBedrooms(detailRequest.getNumberOfBedrooms());
//                        newDetail.setNumberOfBathrooms(detailRequest.getNumberOfBathrooms());
//                        newDetail.setProperty(propertyEntity);
//                        propertyEntity.getPropertyDetails().add(newDetail);
//                    }
//                }
//            }

            propertyEntity = propertyRepository.save(propertyEntity);

            PropertyResponse propertyResponse = new PropertyResponse(propertyEntity);
            return propertyResponse;
        } else {
            throw new IllegalArgumentException("Property with ID " + id + " not found");
        }

//        if (request.getName() != null) propertyEntity.setName(request.getName());
//        if (request.getLocation() != null) propertyEntity.setLocation(request.getLocation());
//        if (request.getTotalShares() != null) {
//            propertyEntity.setTotalShares(request.getTotalShares());
//            propertyEntity.setAvailableShares(request.getTotalShares());
//        }
//        if (request.getRentalIncome() != null) propertyEntity.setRentalIncome(request.getRentalIncome());
//        propertyEntity.setUpdatedAt(new Date());
//        if (request.getCurrentValue()!=null) propertyEntity.setCurrentValue(request.getCurrentValue());
//        if (request.getCurrentValue() != null) {
//            PropertyValueEntity propertyValueEntity = new PropertyValueEntity();
//            propertyValueEntity.setPropertyValue(request.getCurrentValue());
//            propertyValueEntity.setPropertyValue(propertyValueEntity.getAvailableShares());
//            propertyValueEntity.setValueDate(new Date());
//            propertyValueEntity.setProperty(propertyEntity);
//
//            propertyEntity.addToPropertyValues(propertyValueEntity);
//        }
//        propertyEntity.setUpdatedAt(new Date());
//
//        if (request.getPropertyDetails() != null) {
//            List<PropertyDetailEntity> existingDetails = propertyEntity.getPropertyDetails();
//            for (PropertyDetailRequest detailRequest : request.getPropertyDetails()) {
//                PropertyDetailEntity matchingDetail = existingDetails.stream()
//                        .filter(detail -> detail.getLatitude().equals(detailRequest.getLatitude()))
//                        .findFirst()
//                        .orElse(null);
//
//                if (matchingDetail != null) {
//                    if (detailRequest.getLongitude() != null) matchingDetail.setLongitude(detailRequest.getLongitude());
//                    if (detailRequest.getLocationAddress() != null) matchingDetail.setLocationAddress(detailRequest.getLocationAddress());
//                    if (detailRequest.getLocationName() != null) matchingDetail.setLocationName(detailRequest.getLocationName());
//                    if (detailRequest.getDescription() != null) matchingDetail.setDescription(detailRequest.getDescription());
//                    if (detailRequest.getNumberOfShares() != null) matchingDetail.setNumberOfShares(detailRequest.getNumberOfShares());
//                    if (detailRequest.getPropertyPrice() != null) matchingDetail.setPropertyPrice(detailRequest.getPropertyPrice());
//                    if (detailRequest.getPropertySize() != null) matchingDetail.setPropertySize(detailRequest.getPropertySize());
//                    if (detailRequest.getNumberOfBedrooms() != null) matchingDetail.setNumberOfBedrooms(detailRequest.getNumberOfBedrooms());
//                    if (detailRequest.getNumberOfBathrooms() != null) matchingDetail.setNumberOfBathrooms(detailRequest.getNumberOfBathrooms());
//                } else {
//                    PropertyDetailEntity newDetail = new PropertyDetailEntity();
//                    newDetail.setLatitude(detailRequest.getLatitude());
//                    newDetail.setLongitude(detailRequest.getLongitude());
//                    newDetail.setLocationAddress(detailRequest.getLocationAddress());
//                    newDetail.setLocationName(detailRequest.getLocationName());
//                    newDetail.setDescription(detailRequest.getDescription());
//                    newDetail.setNumberOfShares(detailRequest.getNumberOfShares());
//                    newDetail.setPropertyPrice(detailRequest.getPropertyPrice());
//                    newDetail.setPropertySize(detailRequest.getPropertySize());
//                    newDetail.setNumberOfBedrooms(detailRequest.getNumberOfBedrooms());
//                    newDetail.setNumberOfBathrooms(detailRequest.getNumberOfBathrooms());
//                    newDetail.setProperty(propertyEntity);
//
//                    propertyEntity.getPropertyDetails().add(newDetail);
//                }
//            }
//        }
//
//        propertyEntity = propertyRepository.save(propertyEntity);
//        PropertyResponse updateResponse = new PropertyResponse(propertyEntity);
//        return updateResponse;
    }

    public void deletePropertyById(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property with ID " + id + " not found");
        }
        propertyRepository.deleteById(id);
    }

}
