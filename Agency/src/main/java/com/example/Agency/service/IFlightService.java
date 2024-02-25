package com.example.Agency.service;

import com.example.Agency.model.Flight;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IFlightService {
    List<Flight> getFlights();
    List<Flight> findAvailableFlight(LocalDate date,String origin, String destination);
    void saveFlightReservation(Flight flight);
    Flight updateFlight(Flight existingFlight, Flight updatedFlight);
    Optional<Flight> findFlight(Long id);
    boolean validateAndDeleteFlight(Long id);
}
