package com.finalproject.airport.airplane.checkincounter.repository;

import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CheckinCounterRepository extends JpaRepository<CheckinCounter, Integer> {


    CheckinCounter findBycheckinCounterCode(int checkinCounterCode);

    List<CheckinCounter> findByisActive(String y);


    CheckinCounter findByLocation(CheckinCounterLocation  location);

    @Query("SELECT cc.status AS status, COUNT(cc) AS count FROM checkincounter cc GROUP BY cc.status")
    List<Object[]> findStatusCounts();


}
