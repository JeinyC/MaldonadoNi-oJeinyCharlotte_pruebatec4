package com.example.Agency.service;

import com.example.Agency.model.Flight;
import com.example.Agency.model.User;
import com.example.Agency.repository.FlightRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService implements IFlightService {

    private final FlightRepo flightRepo;

    @Autowired
    public FlightService(FlightRepo flightRepo) {
        this.flightRepo = flightRepo;
    }

    @Override
    public List<Flight> getFlights() {
        return flightRepo.findAll();
    }

    @Override
    public List<Flight> findAvailableFlight(LocalDate date, String origin, String destination) {
        return flightRepo.findAvailableFlight(date,origin,destination);
    }

    public Flight findFlightReservation(Flight flightReservation) {

        Flight existingFlight = flightRepo.findAll()
                .stream()
                .filter(f ->
                        f.getOrigin().equals(flightReservation.getOrigin()) &&
                        f.getDestination().equals(flightReservation.getDestination()) &&
                        f.getDate().equals(flightReservation.getDate()))
                .findFirst()
                .orElse(null);

        flightReservation.setId(existingFlight.getId());
        flightReservation.setFlightNumber(existingFlight.getFlightNumber());
        flightReservation.setName(existingFlight.getName());
        flightReservation.setSeatType(existingFlight.getSeatType());
        flightReservation.setPrice(existingFlight.getPrice());
        return flightReservation;
    }

    @Override
    public void saveFlightReservation(Flight flightReservation, Long id, List<User> userList){
        Optional<Flight> optionalFlight = findFlight(id);
        if (optionalFlight.isPresent()) {
            flightReservation.setUserList(userList);
            flightRepo.save(flightReservation);
        }
    }

    @Override
    public Flight updateFlight(Flight existingFlight, Flight updatedFlight) {
        existingFlight.setFlightNumber(updatedFlight.getFlightNumber());
        existingFlight.setName(updatedFlight.getName());
        existingFlight.setOrigin(updatedFlight.getOrigin());
        existingFlight.setDestination(updatedFlight.getDestination());
        existingFlight.setSeatType(updatedFlight.getSeatType());
        existingFlight.setPrice(updatedFlight.getPrice());
        existingFlight.setDate(updatedFlight.getDate());
        flightRepo.save(existingFlight);
        return existingFlight;
    }

    @Override
    public Optional<Flight> findFlight(Long id) {
        return flightRepo.findById(id);
    }

    @Override
    public void saveFlight(List<Flight> flights) {
        flightRepo.saveAll(flights);
    }

    @Override
    public void deleteFlight(Long id) {
        flightRepo.deleteById(id);
    }
}
