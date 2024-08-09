package com.finalproject.airport.airplane.baggageclaim.repository;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaggageClaimRepository  extends CrudRepository<BaggageClaim, Integer> {

    List<BaggageClaim> findByisActive(String y);

    BaggageClaim findBybaggageClaimCode(int baggageClaimCode);

    BaggageClaim findByLocation(BaggageClaimLocation location);

    @Query("SELECT cc.status AS status, COUNT(cc) AS count FROM baggageclamim cc GROUP BY cc.status")
    List<Object[]> findBaggageClaimStatusCounts();

    Optional<BaggageClaim> findByBaggageClaimCode(Integer baggageClaimNumber);
}
