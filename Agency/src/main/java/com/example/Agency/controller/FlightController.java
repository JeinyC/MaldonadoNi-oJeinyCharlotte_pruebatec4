package com.example.Agency.controller;

import com.example.Agency.exception.FlightNotFoundException;
import com.example.Agency.exception.MissingParametersException;
import com.example.Agency.model.*;
import com.example.Agency.service.IFlightService;
import com.example.Agency.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FlightController {

    private final IFlightService iFlightService;
    private final IUserService iUserService;
    private final FlightReservationFactory reservationFactory;

    @Autowired
    public FlightController(IFlightService iFlightService, IUserService iUserService, FlightReservationFactory reservationFactory) {
        this.iFlightService = iFlightService;
        this.iUserService = iUserService;
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
            @RequestParam(name = "origin", required = false) String origin,
            @RequestParam(name = "destination", required = false) String destination
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
                if (iFlightService.validateAndDeleteFlight(id)) {
                    return buildResponse("Flight deleted successfully", HttpStatus.OK);
                } else {
                    return buildResponse("Flight is already reserved", HttpStatus.NOT_ACCEPTABLE);
                }
            } catch (Exception e) {
                return handleGenericException(e);
            }
        } else {
            return buildResponse("Non-existent flight", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/flights/new")
    public ResponseEntity<List<String>> saveFlights(@RequestBody List<Flight> flights) {
        List<String> responses = new ArrayList<>();
        for (Object flight : flights) {
            try {

                Reservation reservation = reservationFactory.createReservation(flight);
                responses.add(reservation.confirmReservation());
            } catch (FlightNotFoundException e) {
                responses.add(e.getMessage());
            } catch (Exception e) {
                responses.add("Error processing reservation");
            }
        }
        return ResponseEntity.ok(responses);
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

