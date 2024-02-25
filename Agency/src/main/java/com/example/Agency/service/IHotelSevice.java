package com.example.Agency.service;

import com.example.Agency.model.Hotel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IHotelSevice {

    List<Hotel> getHotels();
    List<Hotel> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination);
    void saveHotelReservation(Hotel hotel);
    Hotel updateHotel(Hotel existingHotel, Hotel updatedHotel);
    Optional<Hotel> findHotel(Long id);
    boolean validateAndDeleteHotel(Long id);
}
