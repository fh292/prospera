package com.example.prospera.property.service;

import com.example.prospera.property.image.ImageEntity;
import com.example.prospera.property.PropertyRepository;
import com.example.prospera.property.bo.PropertyRequest;
import com.example.prospera.property.bo.PropertyResponse;
import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.property.entity.PropertyValueEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;

    // get all properties
    public List<PropertyResponse> getAllProperties() {
        List<PropertyEntity> propertyEntities = propertyRepository.findAll();
        return propertyEntities.stream()
                .map(PropertyResponse::new)
                .collect(Collectors.toList());
    }

    // get property by id
    public PropertyResponse getPropertyById(Long id) {
        PropertyEntity propertyEntity = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property with ID " + id + " not found"));
        PropertyResponse responseId = new PropertyResponse(propertyEntity);
        return responseId;
    }

    // adding new property
    public PropertyResponse addProperty(PropertyRequest propertyRequest) {
        PropertyEntity propertyEntity = PropertyEntity.builder()
                .totalShares(propertyRequest.getTotalShares())
                .availableShares(propertyRequest.getAvailableShares())
                .rentalIncome(propertyRequest.getRentalIncome())
                .currentValue(propertyRequest.getCurrentValue())
                .typeOfProperty(propertyRequest.getTypeOfProperty())
                .latitude(propertyRequest.getLatitude())
                .longitude(propertyRequest.getLongitude())
                .locationAddress(propertyRequest.getLocationAddress())
                .locationName(propertyRequest.getLocationName())
                .description(propertyRequest.getDescription())
                .propertySize(propertyRequest.getPropertySize())
                .numberOfBedrooms(propertyRequest.getNumberOfBedrooms())
                .numberOfBathrooms(propertyRequest.getNumberOfBathrooms())
                .locationCity(propertyRequest.getLocationCity())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        PropertyValueEntity propertyValueEntity = PropertyValueEntity.builder()
                .propertyValue(propertyRequest.getCurrentValue())
                .availableShares(propertyRequest.getAvailableShares())
                .valueDate(new Date())
                .property(propertyEntity)
                .build();

        propertyEntity.addToPropertyValues(propertyValueEntity);


        propertyEntity = propertyRepository.save(propertyEntity);
        return new PropertyResponse(propertyEntity);
    }

    public void addImagesToProperty(Long propertyId, List<MultipartFile> imagesFiles) {
        if (imagesFiles == null || imagesFiles.isEmpty()) {
            throw new RuntimeException("No images provided for upload.");
        }

        PropertyEntity propertyEntity = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property with ID " + propertyId + " not found"));

        for (MultipartFile file : imagesFiles) {
            try {
                // Validate file type (optional)
                if (!file.getContentType().startsWith("image/")) {
                    throw new RuntimeException("File " + file.getOriginalFilename() + " is not a valid image.");
                }

                // Create and associate image
                ImageEntity image = new ImageEntity();
                image.setName(file.getOriginalFilename());
                image.setType(file.getContentType());
                image.setData(file.getBytes());
                image.setProperty(propertyEntity);

                propertyEntity.addImage(image);
            } catch (Exception e) {
                throw new RuntimeException("Error saving image: " + file.getOriginalFilename() + " - " + e.getMessage(), e);
            }
        }

        propertyRepository.save(propertyEntity);
    }


    // update a property
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Optional<PropertyEntity> propertyOptional = propertyRepository.findById(id);
        if (propertyOptional.isPresent()) {
            PropertyEntity propertyEntity = propertyOptional.get();

            // Update basic fields if they are not null
            if (request.getLocationName() != null)
                propertyEntity.setLocationName(request.getLocationName());
            if (request.getLocationAddress() != null)
                propertyEntity.setLocationAddress(request.getLocationAddress());
            if (request.getLocationCity() != null)
                propertyEntity.setLocationCity(request.getLocationCity());
            if (request.getDescription() != null)
                propertyEntity.setDescription(request.getDescription());
            if (request.getTotalShares() != null)
                propertyEntity.setTotalShares(request.getTotalShares());
            if (request.getRentalIncome() != null)
                propertyEntity.setRentalIncome(request.getRentalIncome());
            if (request.getTypeOfProperty() != null)
                propertyEntity.setTypeOfProperty(request.getTypeOfProperty());
            if (request.getLatitude() != null)
                propertyEntity.setLatitude(request.getLatitude());
            if (request.getLongitude() != null)
                propertyEntity.setLongitude(request.getLongitude());
            if (request.getPropertySize() != null)
                propertyEntity.setPropertySize(request.getPropertySize());
            if (request.getNumberOfBedrooms() != null)
                propertyEntity.setNumberOfBedrooms(request.getNumberOfBedrooms());
            if (request.getNumberOfBathrooms() != null)
                propertyEntity.setNumberOfBathrooms(request.getNumberOfBathrooms());

            // Check if value-tracking fields have changed
            Boolean currentValueChanged = request.getCurrentValue() != null
                    && !request.getCurrentValue().equals(propertyEntity.getCurrentValue());
            Boolean availableSharesChanged = request.getAvailableShares() != null
                    && !request.getAvailableShares().equals(propertyEntity.getAvailableShares());

            // Update value-tracking fields and create history entry if needed
            if (currentValueChanged || availableSharesChanged) {
                if (currentValueChanged)
                    propertyEntity.setCurrentValue(request.getCurrentValue());
                if (availableSharesChanged)
                    propertyEntity.setAvailableShares(request.getAvailableShares());

                PropertyValueEntity propertyValueEntity = PropertyValueEntity.builder()
                        .propertyValue(propertyEntity.getCurrentValue())
                        .availableShares(propertyEntity.getAvailableShares())
                        .valueDate(new Date())
                        .property(propertyEntity)
                        .build();

                propertyEntity.addToPropertyValues(propertyValueEntity);
            }

            propertyEntity.setUpdatedAt(new Date());
            propertyEntity = propertyRepository.save(propertyEntity);
            return new PropertyResponse(propertyEntity);
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
