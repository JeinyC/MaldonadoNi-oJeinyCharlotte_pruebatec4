package com.example.Agency.model;

import com.example.Agency.dto.FlightBookingDTO;
import com.example.Agency.exception.FlightNotFoundException;
import com.example.Agency.service.FlightService;
import com.example.Agency.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class FlightReservationFactory extends AbstractReservationFactory {

    private final FlightService flightService;
    private final IUserService iUserService;

    @Autowired
    public FlightReservationFactory(FlightService flightService, IUserService iUserService) {
        this.flightService = flightService;
        this.iUserService = iUserService;
    }

    @Override
    public FlightBookingDTO createReservation(Object flight){

        Flight flightReservation = flightService.findFlightReservation((Flight)flight);

        if (flightReservation != null) {
            iUserService.saveUser(flightReservation.getUserList());
            flightService.saveFlightReservation(flightReservation, flightReservation.getId(), flightReservation.getUserList());
            return new FlightBookingDTO(
                    flightReservation.getUserList(),
                    flightReservation.getOrigin(),
                    flightReservation.getDestination(),
                    flightReservation.getDate(),
                    flightReservation.getPrice());
        } else {
            throw new FlightNotFoundException("Flight not found for the specified criteria.");
        }
    }
}