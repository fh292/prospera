package com.example.prospera.property.image;

import com.example.prospera.property.entity.PropertyEntity;
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
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

    private String name;

    private String type;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity property;

  }
