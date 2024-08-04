package com.finalproject.airport.airplane.airplane.repository;

import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Integer> {
    List<Airplane> findByisActive(String y);

    Airplane findByairplaneCode(int airplaneCode);

    Airplane findByAirplaneCode(int airplane);


    List<Airplane> findAllByGatenumber(int i);

    List<Airplane> findByGatenumber(int i);
}
