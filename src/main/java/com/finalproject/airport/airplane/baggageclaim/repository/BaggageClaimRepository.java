package com.finalproject.airport.airplane.baggageclaim.repository;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaggageClaimRepository  extends CrudRepository<BaggageClaim, Integer> {

}
