package com.example.Agency.dto;

import com.example.Agency.model.Reservation;
import com.example.Agency.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlightBookingDTO implements Reservation {

    private List<User> userList;
    private String destination;
    private Double price;
    private String flightNumber;
    @Override
    public String confirmReservation() {
        return "Flight : " + flightNumber + " The total price is "  + price * userList.size() + " Destination : " + destination;
    }
}
