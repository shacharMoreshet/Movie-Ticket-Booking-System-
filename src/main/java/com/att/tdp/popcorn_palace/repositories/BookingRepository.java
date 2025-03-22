package com.att.tdp.popcorn_palace.repositories;
import com.att.tdp.popcorn_palace.entities.Booking;
import com.att.tdp.popcorn_palace.entities.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    // Find booking by ID
    Optional<Booking> findById(UUID bookingId);

    // Find all bookings for a specific showtime
    List<Booking> findByShowTime_Id(Long showtimeId);

    // Check if a seat is already booked for a specific showtime
    Optional<Booking> findByShowTimeAndSeatNumber(ShowTime showtime, int seatNumber);
}
