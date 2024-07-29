package com.finalproject.airport.inspection.respository;

import com.finalproject.airport.inspection.entity.InspectionEntity;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface InspectionRepository extends JpaRepository<InspectionEntity, Integer> {


    InspectionEntity findByinspectionCode(int inspectionCode);

    List<InspectionEntity> findByIsActive(String Y);
}
