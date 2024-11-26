package com.example.prospera.investment;

import com.example.prospera.property.entity.PropertyEntity;
import com.example.prospera.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentEntity, Long> {
    Optional<InvestmentEntity> findByUserAndProperty(UserEntity user, PropertyEntity property);

}
