package com.att.tdp.popcorn_palace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long id;
    private String title;
    private String genre;
    private int duration;
    private double rating;
    private int releaseYear;
}