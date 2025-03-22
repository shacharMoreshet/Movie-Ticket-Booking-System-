package com.att.tdp.popcorn_palace.repositories;

import com.att.tdp.popcorn_palace.entities.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long>{
    Optional<ShowTime> findById(Long id);  // Find showtime by ID

    // Custom query to check for overlapping showtimes in the same theater
    List<ShowTime> findByTheaterAndStartTimeBetween(String theater, LocalDateTime startTime, LocalDateTime endTime);
}
