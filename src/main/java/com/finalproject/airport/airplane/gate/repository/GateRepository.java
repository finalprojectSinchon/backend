package com.finalproject.airport.airplane.gate.repository;

import com.finalproject.airport.airplane.gate.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateRepository extends JpaRepository<Gate, Integer> {


    Gate findBygateCode(int gateCode);
}
