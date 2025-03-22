package com.att.tdp.popcorn_palace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;  // Reference to the movie entity

    private double price;
    private String theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Constructor without 'id' field
    public ShowTime(Movie movie, double price, String theater, LocalDateTime startTime, LocalDateTime endTime) {
        this.movie = movie;
        this.price = price;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
