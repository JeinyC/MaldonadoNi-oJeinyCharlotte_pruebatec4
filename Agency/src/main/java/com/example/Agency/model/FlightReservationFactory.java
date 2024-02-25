package com.example.Agency.model;

import com.example.Agency.dto.FlightBookingDTO;
import com.example.Agency.dto.HotelBookingDTO;
import com.example.Agency.exception.FlightNotFoundException;
import com.example.Agency.service.FlightService;
import com.example.Agency.service.IFlightService;
import com.example.Agency.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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