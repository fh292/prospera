package com.example.prospera.property.bo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyValueRequest {
    private Integer propertyValue;
    //private Date valueDate;
}
