package com.example.Agency.model;

import com.example.Agency.dto.HotelBookingDTO;
import com.example.Agency.service.IHotelSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HotelReservationFactory extends AbstractReservationFactory {

    private final IHotelSevice iHotelSevice;

    @Autowired
    public HotelReservationFactory(IHotelSevice iHotelSevice) {
        this.iHotelSevice = iHotelSevice;
    }

    @Override
    public Reservation createReservation(Object newBooking) {
        Hotel hotel = (Hotel) newBooking;
        iHotelSevice.saveHotelReservation(hotel);
        return  new HotelBookingDTO(hotel.getHotelCode(), hotel.getUserList(), hotel.getPrice(), hotel.getNights());
    }
}
