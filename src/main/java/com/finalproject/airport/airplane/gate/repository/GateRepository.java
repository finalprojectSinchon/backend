package com.finalproject.airport.airplane.gate.repository;

import com.finalproject.airport.airplane.gate.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GateRepository extends JpaRepository<Gate, Integer> {


    Gate findBygateCode(int gateCode);


    List<Gate> findByisActive(String y);


    @Query("SELECT location FROM gate")
    List<Integer> findAlllocations();

    @Query("SELECT gateCode FROM gate WHERE location = location")
    Integer findbylocation(String location);
}
