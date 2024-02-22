package com.example.Agency.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hotelCode;
    private String name;
    private String city;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Double roomType;
    private Double price;
    private int nights;
    private boolean isBooked;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hotelReservation_id")
    private List<User> userList;
}
