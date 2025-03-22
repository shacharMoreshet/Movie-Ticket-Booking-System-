package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.MovieDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.exceptions.ObjectAlreadyExist;
import com.att.tdp.popcorn_palace.exceptions.ObjectNotFoundException;
import com.att.tdp.popcorn_palace.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    // Convert Movie entity to MovieDTO
    private MovieDTO mapToDTO(Movie movie) {
        return new MovieDTO(movie.getId(), movie.getTitle(), movie.getGenre(), movie.getDuration(), movie.getRating(), movie.getReleaseYear());
    }

    // Convert MovieDTO to Movie entity
    private Movie mapToEntity(MovieDTO movieDTO) {
        return new Movie(movieDTO.getTitle(), movieDTO.getGenre(), movieDTO.getDuration(), movieDTO.getRating(), movieDTO.getReleaseYear());
    }

    // Get all movies
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get a single movie by title
    public MovieDTO getMovieByTitle(String title) {
        Movie movie = movieRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDTO(movie);
    }

    // Add a new movie
    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = mapToEntity(movieDTO);
        // Check if movie with the same title already exists
        Optional<Movie> existingMovie = movieRepository.findByTitle(movie.getTitle());
        if (existingMovie.isPresent()) {
            throw new ObjectAlreadyExist("Movie with the title '" + movie.getTitle() + "' already exists");
        }
        Movie savedMovie = movieRepository.save(movie);
        return mapToDTO(savedMovie);
    }

    // Update an existing movie by title
    public MovieDTO updateMovie(String title, MovieDTO movieDTO) {
        Movie existingMovie = movieRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Movie not found"));
        existingMovie.setTitle(movieDTO.getTitle());
        existingMovie.setGenre(movieDTO.getGenre());
        existingMovie.setDuration(movieDTO.getDuration());
        existingMovie.setRating(movieDTO.getRating());
        existingMovie.setReleaseYear(movieDTO.getReleaseYear());
        Movie updatedMovie = movieRepository.save(existingMovie);
        return mapToDTO(updatedMovie);
    }

    // Delete a movie by title
    public void deleteMovie(String title) {
        Movie movie = movieRepository.findByTitle(title).orElseThrow(() ->
                new ObjectNotFoundException("Movie not found"));
        movieRepository.delete(movie);
    }
}
