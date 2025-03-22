package com.att.tdp.popcorn_palace.controllers;

import com.att.tdp.popcorn_palace.dtos.ShowTimeDTO;
import com.att.tdp.popcorn_palace.exceptions.ObjectNotFoundException;
import com.att.tdp.popcorn_palace.services.ShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/showtimes")
public class ShowTimeController {

    private final ShowTimeService showtimeService;

    @GetMapping("/{showtimeId}")
    public ResponseEntity<ShowTimeDTO> getShowtimeById(@PathVariable Long showtimeId) {
        ShowTimeDTO showtime = showtimeService.getShowtimeById(showtimeId);
        return new ResponseEntity<>(showtime, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ShowTimeDTO> addShowtime(@RequestBody ShowTimeDTO showtimeDTO) {
        ShowTimeDTO createdShowtime = showtimeService.createShowtime(showtimeDTO);
        return new ResponseEntity<>(createdShowtime, HttpStatus.CREATED);
    }

    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<ShowTimeDTO> updateShowtime(@PathVariable Long showtimeId, @RequestBody ShowTimeDTO showtimeDTO) {
        ShowTimeDTO updatedShowtime = showtimeService.updateShowtime(showtimeId, showtimeDTO);
        return new ResponseEntity<>(updatedShowtime, HttpStatus.OK);
    }

    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long showtimeId) {
        showtimeService.deleteShowtime(showtimeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
