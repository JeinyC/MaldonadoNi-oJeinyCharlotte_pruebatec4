package com.example.Agency.repository;

import com.example.Agency.model.Flight;
import com.example.Agency.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepo extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.date = :date AND f.origin = :origin AND f.destination = :destination")
    List<Flight> findAvailableFlight(
            @Param("date") LocalDate date,
            @Param("origin") String origin,
            @Param("destination") String destination);

}
