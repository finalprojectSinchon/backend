package com.finalproject.airport.airplane.airplane.repository;

import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartureAirplaneRepository extends JpaRepository<DepartureAirplane, Integer> {
    List<DepartureAirplane> findByisActive(String y);

    DepartureAirplane findByairplaneCode(int airplaneCode);

    DepartureAirplane findByAirplaneCode(int airplane);


    List<DepartureAirplane> findAllByGatenumber(int i);

    List<DepartureAirplane> findByGatenumber(int i);
}
