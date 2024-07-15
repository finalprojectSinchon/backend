package com.finalproject.airport.equipment.repository;

import com.finalproject.airport.equipment.entity.EquipmentEntity;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {


}
