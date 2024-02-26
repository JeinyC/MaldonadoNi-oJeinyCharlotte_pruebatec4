package com.example.Agency.service;

import com.example.Agency.model.Hotel;
import com.example.Agency.model.User;
import com.example.Agency.repository.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService implements IHotelSevice {
    private final HotelRepo hotelRepo;

    @Autowired
    public HotelService(HotelRepo hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    @Override
    public List<Hotel> getHotels() {
        return hotelRepo.findAll();
    }

    @Override
    public List<Hotel> findAvailableHotels(LocalDate dateFrom, LocalDate dateTo, String city) {
        return hotelRepo.findAvailableHotels(dateFrom, dateTo, city);
    }

    @Override
    public void saveHotelReservation(Hotel hotel) {
        hotel.setNights((int) ChronoUnit.DAYS.between(hotel.getDateFrom(), hotel.getDateTo()));
        hotel.setBooked(true);
        this.hotelRepo.save(hotel);
    }

    @Override
    public Hotel updateHotel(Hotel existingHotel, Hotel updatedHotel) {
        existingHotel.setHotelCode(updatedHotel.getHotelCode());
        existingHotel.setName(updatedHotel.getName());
        existingHotel.setCity(updatedHotel.getCity());
        existingHotel.setDateFrom(updatedHotel.getDateFrom());
        existingHotel.setDateTo(updatedHotel.getDateTo());
        existingHotel.setRoomType(updatedHotel.getRoomType());
        existingHotel.setPrice(updatedHotel.getPrice());
        existingHotel.setNights(updatedHotel.getNights());
        existingHotel.setBooked(updatedHotel.isBooked());

        List<User> existingUsers = existingHotel.getUserList();
        List<User> updatedUsers = updatedHotel.getUserList();
        existingUsers.removeIf(user -> !updatedUsers.contains(user));

        for (User updatedUser : updatedUsers) {
            if (!existingUsers.contains(updatedUser)) {
                existingUsers.add(updatedUser);
            }
        }
        hotelRepo.save(existingHotel);
        return existingHotel;
    }

    @Override
    public Optional<Hotel> findHotel(Long id) {
        return hotelRepo.findById(id);
    }

    @Override
    public boolean validateAndDeleteHotel(Long id) {
        Optional<Hotel> existingHotel = findHotel(id);

        if (existingHotel.get().getUserList().isEmpty()) {
            hotelRepo.deleteById(id);
            return true;
        }
        return false;
    }
}

