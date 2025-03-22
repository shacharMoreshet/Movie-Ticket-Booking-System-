package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.ShowTimeDTO;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.entities.ShowTime;
import com.att.tdp.popcorn_palace.exceptions.ObjectNotFoundException;
import com.att.tdp.popcorn_palace.repositories.MovieRepository;
import com.att.tdp.popcorn_palace.repositories.ShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowTimeService {

    private final ShowTimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    private ShowTimeDTO mapToDTO(ShowTime showtime) {
        return new ShowTimeDTO(
                showtime.getId(),
                showtime.getPrice(),
                showtime.getMovie().getId(),
                showtime.getTheater(),
                showtime.getStartTime(),
                showtime.getEndTime()
        );
    }

    private ShowTime mapToEntity(ShowTimeDTO showtimeDTO) {
        Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                .orElseThrow(() -> new ObjectNotFoundException("Movie not found"));

        return new ShowTime(
                movie,
                showtimeDTO.getPrice(),
                showtimeDTO.getTheater(),
                showtimeDTO.getStartTime(),
                showtimeDTO.getEndTime()
        );
    }

    public List<ShowTimeDTO> getAllShowtimes() {
        return showtimeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ShowTimeDTO getShowtimeById(Long showtimeId) {
        ShowTime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ObjectNotFoundException("Showtime not found"));
        return mapToDTO(showtime);
    }

    public ShowTimeDTO createShowtime(ShowTimeDTO showtimeDTO) {
        ShowTime showtime = mapToEntity(showtimeDTO);
        ShowTime savedShowtime = showtimeRepository.save(showtime);
        return mapToDTO(savedShowtime);
    }

    public ShowTimeDTO updateShowtime(Long showtimeId, ShowTimeDTO showtimeDTO) {
        ShowTime existingShowtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ObjectNotFoundException("Showtime not found"));

        Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                .orElseThrow(() -> new ObjectNotFoundException("Movie not found"));

        existingShowtime.setMovie(movie);
        existingShowtime.setTheater(showtimeDTO.getTheater());
        existingShowtime.setStartTime(showtimeDTO.getStartTime());
        existingShowtime.setEndTime(showtimeDTO.getEndTime());
        existingShowtime.setPrice(showtimeDTO.getPrice());

        ShowTime updatedShowtime = showtimeRepository.save(existingShowtime);
        return mapToDTO(updatedShowtime);
    }

    public void deleteShowtime(Long showtimeId) {
        ShowTime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ObjectNotFoundException("Showtime not found"));
        showtimeRepository.delete(showtime);
    }
}
