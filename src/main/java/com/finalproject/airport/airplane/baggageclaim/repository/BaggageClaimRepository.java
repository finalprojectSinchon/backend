package com.finalproject.airport.airplane.baggageclaim.repository;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggageClaimRepository  extends CrudRepository<BaggageClaim, Integer> {

    List<BaggageClaim> findByisActive(String y);

    BaggageClaim findBybaggageClaimCode(int baggageClaimCode);

    @Query("SELECT baggageClaimCode FROM baggageclamim WHERE location = :location")
    Integer findbylocation(BaggageClaimLocation location);
}
