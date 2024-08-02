package com.finalproject.airport.airplane.gate.repository;

import com.finalproject.airport.airplane.gate.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface GateRepository extends JpaRepository<Gate, Integer> {


    Gate findBygateCode(int gateCode);


    List<Gate> findByisActive(String y);


    @Query("SELECT location FROM gate")
    List<Integer> findAlllocations();


    Gate findByLocation(Integer location);


    Gate findAllBygateCode(int i);
}
