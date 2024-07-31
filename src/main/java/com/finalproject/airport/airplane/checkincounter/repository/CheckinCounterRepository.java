package com.finalproject.airport.airplane.checkincounter.repository;

import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckinCounterRepository extends JpaRepository<CheckinCounter, Integer> {


    CheckinCounter findBycheckinCounterCode(int checkinCounterCode);

    List<CheckinCounter> findByisActive(String y);

    @Query("SELECT checkinCounterCode FROM checkincounter WHERE location = location")
    Integer findbylocation(String location);
}
