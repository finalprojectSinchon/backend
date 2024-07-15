package com.finalproject.airport.airplane.checkincounter.repository;

import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckinCounterRepository extends JpaRepository<CheckinCounter, Integer> {



}
