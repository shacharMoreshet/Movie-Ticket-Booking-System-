package com.att.tdp.popcorn_palace.controllers;

import com.att.tdp.popcorn_palace.dtos.BookingDTO;
import com.att.tdp.popcorn_palace.exceptions.ObjectAlreadyExist;
import com.att.tdp.popcorn_palace.exceptions.ObjectNotFoundException;
import com.att.tdp.popcorn_palace.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController extends ErrorHandlingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Map<String, UUID>> bookTicket(@RequestBody BookingDTO bookingDTO) {
        UUID createdBookingId = bookingService.bookTicket(bookingDTO);
        return ResponseEntity.ok(Map.of("bookingId", createdBookingId));
    }
}