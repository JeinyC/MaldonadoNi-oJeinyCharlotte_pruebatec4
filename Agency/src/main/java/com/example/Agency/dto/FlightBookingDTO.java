package com.example.Agency.dto;

import com.example.Agency.model.Reservation;
import com.example.Agency.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlightBookingDTO implements Reservation {

    private List<User> userList;
    private String origin;
    private String destination;
    private LocalDate date;
    private double price;

    @Override
    public String confirmReservation() {
        return "The total price is "  + price * userList.size();
    }
}
