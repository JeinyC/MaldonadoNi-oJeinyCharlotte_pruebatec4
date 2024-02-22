package com.example.Agency.service;

import com.example.Agency.dto.HotelBookingDTO;
import com.example.Agency.model.Hotel;
import com.example.Agency.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IHotelSevice {

    List<Hotel> getHotels();
    List<Hotel> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String destination);
    Hotel findHotelByCode(String hotelCode);
    void saveHotelReservation( Hotel hotel);
    Hotel updateHotel(Hotel existingHotel, Hotel updatedHotel);
    void saveHotel(List<Hotel>  hotel);
    void deleteHotel(Long id);
    Optional<Hotel> findHotel(Long id);
    void validateAndDeleteHotel(Long id);
}
