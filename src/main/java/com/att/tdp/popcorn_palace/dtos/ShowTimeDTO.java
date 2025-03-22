package com.att.tdp.popcorn_palace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowTimeDTO {

    private Long id;
    private Double price;
    private Long movieId;  // Refers to Movie by its ID
    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
