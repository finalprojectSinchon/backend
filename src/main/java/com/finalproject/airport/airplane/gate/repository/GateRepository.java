package com.finalproject.airport.airplane.gate.repository;

import com.finalproject.airport.airplane.gate.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Integer> {


    Gate findBygateCode(int gateCode);


    List<Gate> findByisActive(String y);


    @Query("SELECT gateCode FROM gate")
    List<Integer> findAlllocations();


    Gate findByLocation(Integer location);



    Optional<Gate> findByGateCode(Integer gateNumber);

    @Query("SELECT cc.status AS status, COUNT(cc) AS count FROM gate cc GROUP BY cc.status")
    List<Object[]> findGateStatusCounts();


    List<Gate> findByGateCodeBetween(int i, int i1);
}
