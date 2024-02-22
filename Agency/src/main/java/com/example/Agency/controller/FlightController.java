package com.example.Agency.controller;

import com.example.Agency.exception.FlightNotFoundException;
import com.example.Agency.exception.MissingParametersException;
import com.example.Agency.model.*;
import com.example.Agency.service.FlightService;
import com.example.Agency.service.IFlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class FlightController {

    private final IFlightService iFlightService;
    private final FlightReservationFactory reservationFactory;

    @Autowired
    public FlightController(IFlightService iFlightService, FlightReservationFactory reservationFactory) {
        this.iFlightService = iFlightService;
        this.reservationFactory = reservationFactory;
    }

    @GetMapping("/flights")
    public ResponseEntity<?> getFlights() {
        List<Flight> flights = iFlightService.getFlights();
        if (!flights.isEmpty()) {
            return ResponseEntity.ok(flights);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No flights available");
        }
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<?> getFlightById(@PathVariable Long id) {
        Optional<Flight> flight = iFlightService.findFlight(id);
        if (flight.isPresent()) {
            return ResponseEntity.ok(flight.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No flight available with Id " + id);
        }
    }

    @GetMapping("/flights/searchByDateAndPlace")
    public ResponseEntity<?> getFlightByDateAndPlace(
            @RequestParam(name = "date", required = false) LocalDate date,
            @RequestParam(name = "origin",  required = false) String origin,
            @RequestParam(name = "destination",  required = false) String destination
    ) {
        if (date == null || origin == null || destination == null) {
            throw new MissingParametersException("All three parameters are required: date, origin and destination");
        }
        List<Flight> flights = iFlightService.findAvailableFlight(date, origin, destination);
        if (flights.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No flights found matching the criteria");
        } else {
            return ResponseEntity.ok(flights);
        }
    }

    @PutMapping("/flight-booking/new")
    public ResponseEntity<String> flightBooking(@RequestBody Flight flight) {
        Optional<Flight> existingFlight = iFlightService.findFlight(flight.getId());
        if (existingFlight.isPresent()) {
            try {
                Reservation reservation = reservationFactory.createReservation(flight);
                reservation.confirmReservation();
                return buildResponse("Successful flight reservation", HttpStatus.OK);
            } catch (Exception e) {
                return handleGenericException(e);
            }
        } else {
            return buildResponse("Non-existent flight", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("flights/edit/{id}")
    public ResponseEntity<String> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        Optional<Flight> existingFlight = iFlightService.findFlight(id);
        if (existingFlight.isPresent()) {
            try {
                iFlightService.updateFlight(existingFlight.get(), updatedFlight);
                return buildResponse("Successfully edited flight", HttpStatus.OK);
            } catch (Exception e) {
                return handleGenericException(e);
            }
        } else {
            return buildResponse("Non-existent flight", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/flights/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        Optional<Flight> existingFlight = iFlightService.findFlight(id);
        if (existingFlight.isPresent()) {
            try {
                iFlightService.deleteFlight(id);
                return buildResponse("Flight deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                return handleGenericException(e);
            }
        } else {
            return buildResponse("Non-existent flight", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/flights/new")
    public ResponseEntity<String> saveFlights(@RequestBody List<Flight> flights) {
        try {
            iFlightService.saveFlight(flights);
            return buildResponse("Flight added to the system", HttpStatus.OK);
        } catch (Exception e) {
            return handleGenericException(e);
        }
    }

    private ResponseEntity<String> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler(MissingParametersException.class)
    public ResponseEntity<String> handleMissingParametersException(MissingParametersException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<String> handleFlightNotFoundException(FlightNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

