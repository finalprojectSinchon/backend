package com.finalproject.airport.equipment.repository;

import com.finalproject.airport.equipment.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface EquipmentRepository extends JpaRepository<EquipmentEntity,Integer> {


}
