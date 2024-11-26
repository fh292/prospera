package com.example.prospera.property.bo;
import java.util.Date;

import com.example.prospera.property.entity.PropertyValueEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PropertyValueResponse {
    private Long id;
    private Integer propertyValue;
    private Double availableShares;
    private Date date;

    public PropertyValueResponse(PropertyValueEntity propertyValueEntity) {
        this.id = propertyValueEntity.getId();
        this.propertyValue = propertyValueEntity.getPropertyValue();
        this.availableShares = propertyValueEntity.getAvailableShares();
        this.date = propertyValueEntity.getValueDate();
    }
}
