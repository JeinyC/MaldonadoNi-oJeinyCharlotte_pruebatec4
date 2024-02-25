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
public class HotelBookingDTO implements Reservation {

    private String hotelCode;
    private List<User> userList;
    private Double price;
    private int nights;

    @Override
    public String confirmReservation() {
        return "The total price for " + hotelCode + " is " + (nights * price) * userList.size();
    }
}
