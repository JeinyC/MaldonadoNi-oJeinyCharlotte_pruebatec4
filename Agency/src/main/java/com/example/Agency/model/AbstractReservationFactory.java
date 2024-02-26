package com.example.Agency.model;

import org.springframework.stereotype.Component;

@Component
public abstract class AbstractReservationFactory {

    public abstract Reservation createReservation(Object newBooking);
}
