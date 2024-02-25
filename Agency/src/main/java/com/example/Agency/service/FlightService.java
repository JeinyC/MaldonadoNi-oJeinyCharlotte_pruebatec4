package com.example.Agency.service;

import com.example.Agency.model.Flight;
import com.example.Agency.model.Hotel;
import com.example.Agency.model.User;
import com.example.Agency.repository.FlightRepo;
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
        return flightRepo.findAvailableFlight(date, origin, destination);
    }

    @Override
    public void saveFlightReservation(Flight flight) {
        this.flightRepo.save(flight);
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

        // Eliminar usuarios desasociados
        List<User> existingUsers = existingFlight.getUserList();
        List<User> updatedUsers = updatedFlight.getUserList();

        existingUsers.removeIf(user -> !updatedUsers.contains(user));

        // Añadir nuevos usuarios
        for (User updatedUser : updatedUsers) {
            if (!existingUsers.contains(updatedUser)) {
                existingUsers.add(updatedUser);
            }
        }

        flightRepo.save(existingFlight);
        return existingFlight;
    }

    @Override
    public Optional<Flight> findFlight(Long id) {
        return flightRepo.findById(id);
    }

    @Override
    public boolean validateAndDeleteFlight(Long id) {
        Optional<Flight> existingHotel = findFlight(id);
        if (existingHotel.get().getUserList().isEmpty()) {
            flightRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
