package com.att.tdp.popcorn_palace.controllers;

import com.att.tdp.popcorn_palace.dtos.MovieDTO;
import com.att.tdp.popcorn_palace.exceptions.ObjectAlreadyExist;
import com.att.tdp.popcorn_palace.exceptions.ObjectNotFoundException;
import com.att.tdp.popcorn_palace.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController extends ErrorHandlingController {

    private final MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{movieTitle}")
    public ResponseEntity<MovieDTO> getMovieByTitle(@PathVariable String movieTitle) {
        MovieDTO movie = movieService.getMovieByTitle(movieTitle);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = movieService.createMovie(movieDTO);
        return new ResponseEntity<>(createdMovie, HttpStatus.OK);
    }

    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable String movieTitle, @RequestBody MovieDTO movieDTO) {
        MovieDTO updatedMovie = movieService.updateMovie(movieTitle, movieDTO);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String movieTitle) {
        movieService.deleteMovie(movieTitle);
        return new ResponseEntity<>(HttpStatus.OK); // Changed to OK
    }
}
