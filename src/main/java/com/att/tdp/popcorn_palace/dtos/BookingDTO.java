package com.att.tdp.popcorn_palace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private UUID bookingId;   // Unique identifier for booking
    private Long showtimeId;  // References the ShowTime ID
    private int seatNumber;   // Seat number being booked
    private UUID userId;      // User's unique ID
}
