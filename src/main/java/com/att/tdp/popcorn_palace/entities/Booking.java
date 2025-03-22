package com.att.tdp.popcorn_palace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Unique booking ID
    private UUID bookingId;

    @ManyToOne
    @JoinColumn(name = "showtime_id", referencedColumnName = "id", nullable = false)
    private ShowTime showTime; // Each booking is associated with a ShowTime

    private int seatNumber;

    @Column(nullable = false)
    private UUID userId; // Unique identifier for the user

    // Constructor without 'bookingId'
    public Booking(ShowTime showTime, int seatNumber, UUID userId) {
        this.showTime = showTime;
        this.seatNumber = seatNumber;
        this.userId = userId;
    }
}