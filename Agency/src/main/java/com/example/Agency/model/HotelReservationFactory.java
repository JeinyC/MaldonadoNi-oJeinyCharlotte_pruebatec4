package com.example.Agency.model;

import com.example.Agency.dto.HotelBookingDTO;
import com.example.Agency.exception.FlightNotFoundException;
import com.example.Agency.exception.HotelNotFoundException;
import com.example.Agency.service.IHotelSevice;
import com.example.Agency.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;


@Component
public class HotelReservationFactory extends AbstractReservationFactory {

    private final IHotelSevice iHotelSevice;
    private final IUserService iUserService;

    @Autowired
    public HotelReservationFactory(IHotelSevice iHotelSevice, IUserService iUserService) {
        this.iHotelSevice = iHotelSevice;
        this.iUserService = iUserService;
    }

    @Override
    public Reservation createReservation(Object hotel) {// Hotel obj

        Hotel hotelDataBase = (Hotel) hotel;
        iHotelSevice.saveHotelReservation(hotelDataBase);
        return new HotelBookingDTO(hotelDataBase.getHotelCode(), hotelDataBase.getUserList(), hotelDataBase.getPrice(), hotelDataBase.getNights(), hotelDataBase.getId());
    }
}
