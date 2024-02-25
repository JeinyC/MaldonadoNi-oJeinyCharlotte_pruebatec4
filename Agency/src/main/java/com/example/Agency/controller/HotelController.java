package com.example.Agency.controller;

import com.example.Agency.dto.HotelBookingDTO;
import com.example.Agency.exception.HotelException;
import com.example.Agency.exception.HotelNotFoundException;
import com.example.Agency.exception.HotelReservationException;
import com.example.Agency.exception.MissingParametersException;
import com.example.Agency.model.Hotel;
import com.example.Agency.model.HotelReservationFactory;
import com.example.Agency.model.Reservation;
import com.example.Agency.model.User;
import com.example.Agency.service.IHotelSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class HotelController {

    private final IHotelSevice iHotelSevice;
    private final HotelReservationFactory reservationFactory;

    @Autowired
    public HotelController(IHotelSevice iHotelSevice, HotelReservationFactory reservationFactory) {
        this.iHotelSevice = iHotelSevice;
        this.reservationFactory = reservationFactory;
    }

    @GetMapping("/hotels")
    public ResponseEntity<?> getHotels() {
        List<Hotel> hotels = iHotelSevice.getHotels();
        if (!hotels.isEmpty()) {
            return ResponseEntity.ok(hotels);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hotels available");
        }
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id) {
        Optional<Hotel> hotel = iHotelSevice.findHotel(id);
        if (hotel.isPresent()) {
            return ResponseEntity.ok(hotel.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hotel available with Id " + id);
        }
    }

    @GetMapping("/hotels/searchByDateAndCity")
    public ResponseEntity<?> searchHotels(
            @RequestParam(name = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(name = "city", required = false) String city
    ) {
        if (dateFrom == null || dateTo == null || city == null) {
            throw new MissingParametersException("All three parameters are required: dateFrom, dateTo and city");
        }
        List<Hotel> hotels = iHotelSevice.findAvailableHotels(dateFrom, dateTo, city);
        if (!hotels.isEmpty()) {
            return ResponseEntity.ok(hotels);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hotels found matching the criteria");
        }
    }

    @PostMapping("/hotels/new")
    public ResponseEntity<List<String>> saveHotel(@RequestBody List<Hotel> hotels) {
        List<String> responses = new ArrayList<>();
        for (Object hotel : hotels) {
            try {
                Reservation reservation = reservationFactory.createReservation(hotel);
                responses.add(reservation.confirmReservation());
            } catch (HotelNotFoundException e) {
                responses.add(e.getMessage());
            } catch (Exception e) {
                responses.add("Error processing reservation");
                e.printStackTrace();  // Imprimir la pila de excepciones para depuración
                responses.add("Error al procesar la reserva: " + e.getMessage());
            }
        }
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/hotels/delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        try {
            if (iHotelSevice.validateAndDeleteHotel(id)) {
                return buildResponse("Hotel deleted successfully", HttpStatus.OK);
            } else {
                return buildResponse("Hotel is already reserved", HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (HotelNotFoundException e) {
            return buildResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (HotelReservationException e) {
            return buildResponse(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return handleGenericException(e);
        }
    }

    @PutMapping("hotels/edit/{id}")
    public ResponseEntity<String> updateHotel(@PathVariable Long id, @RequestBody Hotel updatedHotel) {
        Optional<Hotel> existingHotel = iHotelSevice.findHotel(id);
        if (existingHotel.isPresent()) {
            try {
                iHotelSevice.updateHotel(existingHotel.get(), updatedHotel);
                return buildResponse("Successfully edited hotel", HttpStatus.OK);
            } catch (Exception e) {
                return handleGenericException(e);
            }
        } else {
            return buildResponse("Non-existent hotel", HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<String> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler(MissingParametersException.class)
    public ResponseEntity<String> handleMissingParametersException(MissingParametersException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<String> handleHotelNotFoundException(HotelNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HotelException.class)
    public ResponseEntity<String> handleHotelException(HotelException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
