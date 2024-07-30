package com.finalproject.airport.airplane.gate.repository;

import com.finalproject.airport.airplane.gate.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GateRepository extends JpaRepository<Gate, Integer> {


    Gate findBygateCode(int gateCode);


    List<Gate> findByisActive(String y);


    @Query("SELECT location FROM gate")
    List<Gate> findAlllocations();
}
