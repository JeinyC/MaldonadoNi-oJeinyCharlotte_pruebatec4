package com.example.Agency.model;

import com.example.Agency.exception.FlightException;
import com.example.Agency.exception.HotelException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractReservationFactory {

    public abstract Reservation createReservation(Object newBooking);
}
