package com.example.Agency.model;

import com.example.Agency.dto.FlightBookingDTO;
import com.example.Agency.service.IFlightService;
import org.springframework.stereotype.Component;

@Component
public class FlightReservationFactory extends AbstractReservationFactory {

    private final IFlightService iFlightService;

    public FlightReservationFactory(IFlightService iFlightService) {
        this.iFlightService = iFlightService;
    }

    @Override
    public Reservation createReservation(Object newBooking){
        Flight flight = (Flight) newBooking;
        iFlightService.saveFlightReservation(flight);
        return  new FlightBookingDTO(flight.getUserList(),flight.getDestination(),flight.getPrice(), flight.getFlightNumber());
    }
}