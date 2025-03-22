package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.BookingDTO;
import com.att.tdp.popcorn_palace.entities.Booking;
import com.att.tdp.popcorn_palace.entities.ShowTime;
import com.att.tdp.popcorn_palace.exceptions.ObjectAlreadyExist;
import com.att.tdp.popcorn_palace.exceptions.ObjectNotFoundException;
import com.att.tdp.popcorn_palace.repositories.BookingRepository;
import com.att.tdp.popcorn_palace.repositories.ShowTimeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowTimeRepository showTimeRepository;

    private Booking mapToEntity(BookingDTO bookingDTO, ShowTime showTime) {
        return new Booking(
                null,
                showTime,
                bookingDTO.getSeatNumber(),
                bookingDTO.getUserId()
        );
    }

    public UUID bookTicket(BookingDTO bookingDTO) {
        ShowTime showTime = showTimeRepository.findById(bookingDTO.getShowtimeId())
                .orElseThrow(() -> new ObjectNotFoundException("Showtime not found"));

        // Check if the seat is already booked
        bookingRepository.findByShowTimeAndSeatNumber(showTime, bookingDTO.getSeatNumber())
                .ifPresent(existingBooking -> {
                    throw new ObjectAlreadyExist("Seat " + bookingDTO.getSeatNumber() + " is already booked.");
                });

        Booking newBooking = mapToEntity(bookingDTO, showTime);
        Booking savedBooking = bookingRepository.save(newBooking);
        return savedBooking.getBookingId();
    }
}
