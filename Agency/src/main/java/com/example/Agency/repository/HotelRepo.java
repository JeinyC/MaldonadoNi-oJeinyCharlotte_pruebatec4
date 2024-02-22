package com.example.Agency.repository;

import com.example.Agency.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE h.dateFrom <= :dateTo AND h.dateTo >= :dateFrom AND h.city = :city")
    List<Hotel> findAvailableHotels(
            @Param("dateTo") LocalDate dateTo,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("city") String city);
}
