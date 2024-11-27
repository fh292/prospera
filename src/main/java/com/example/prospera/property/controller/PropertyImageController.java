package com.example.prospera.property.controller;

import com.example.prospera.property.image.ImageRepository;
import com.example.prospera.property.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/properties/")
public class PropertyImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/{propertyId}/upload-images")
    public ResponseEntity<String> uploadImagesToProperty(
            @PathVariable Long propertyId,
            @RequestParam("images") List<MultipartFile> images) {
        try {
            propertyService.addImagesToProperty(propertyId, images);
            return ResponseEntity.ok("Images uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while uploading images.");
        }
    }
}
