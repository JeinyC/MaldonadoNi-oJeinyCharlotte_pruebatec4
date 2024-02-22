package com.example.Agency.service;

import com.example.Agency.exception.FlightException;
import com.example.Agency.model.Flight;
import com.example.Agency.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IFlightService {
    List<Flight> getFlights();
    List<Flight> findAvailableFlight(LocalDate date,String origin, String destination);
    Flight findFlightReservation(Flight flightReservation);
    void saveFlightReservation(Flight flightReservation, Long id, List<User> userList) throws FlightException;
    Flight updateFlight(Flight existingFlight, Flight updatedFlight);
    Optional<Flight> findFlight(Long id);
    void saveFlight(List<Flight> flight);
    void deleteFlight(Long id);
}
