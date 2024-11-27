package com.example.prospera.property;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.prospera.property.PropertyRepository;
import com.example.prospera.property.service.PropertyService;
import com.example.prospera.property.entity.PropertyEntity;

@Component
public class PropertyScheduler {

  private final PropertyRepository propertyRepository;
  private PropertyService propertyService;

  public PropertyScheduler(PropertyRepository propertyRepository, PropertyService propertyService) {
    this.propertyRepository = propertyRepository;
    this.propertyService = propertyService;
  }

  @Scheduled(cron = "0 1 * * * ?")
  public void propertiesValueCRON() {
    System.out.println("Properties Value CRON Job Started");
    List<PropertyEntity> properties = propertyRepository.findAll();
    for (PropertyEntity property : properties) {
      // random percentage change in property value between -5% and 5%
      double percentageChange = Math.random() * 10 - 5;
      propertyService.changePropertyValueByPercentage(property.getId(), percentageChange);
      System.out.println("Property ID: " + property.getId() + " - Value changed by: " + percentageChange + "%"
          + " - New Value: " + property.getCurrentValue());
    }
  }
}