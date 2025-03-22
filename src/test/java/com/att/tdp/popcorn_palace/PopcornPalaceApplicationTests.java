package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.entities.ShowTime;
import com.att.tdp.popcorn_palace.repositories.BookingRepository;
import com.att.tdp.popcorn_palace.repositories.MovieRepository;
import com.att.tdp.popcorn_palace.repositories.ShowTimeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PopcornPalaceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ShowTimeRepository showTimeRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@BeforeEach
	public void setUp() {
		// Insert test data into H2 database before each test case
		Movie movie1 = new Movie("Movie1", "Action", 120, 8.7, 2025);
		movieRepository.save(movie1);

		ShowTime showTime = new ShowTime(movie1, 50.2, "Sample Theater",
				LocalDateTime.parse("2025-02-14T11:47:46.125405"),
				LocalDateTime.parse("2025-02-14T14:47:46.125405"));
		showTimeRepository.save(showTime);

		// Insert another movie (Movie2) which is not connected to ShowTime
		Movie movie2 = new Movie("Movie2", "Comedy", 100, 7.5, 2024);
		movieRepository.save(movie2);
	}

	@AfterEach
	public void tearDown() {
		// Clear data to prevent side effects between test cases
		bookingRepository.deleteAll();
		showTimeRepository.deleteAll();
		movieRepository.deleteAll();
	}

	// MovieController Tests
	@Test
	public void testGetAllMovies() throws Exception {
		mockMvc.perform(get("/movies/all"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Movie1"));
	}

	@Test
	public void testAddMovie() throws Exception {
		String newMovieJson = "{ \"title\": \"New Movie\", \"genre\": \"Action\", \"duration\": 120, \"rating\": 8.7, \"releaseYear\": 2025 }";

		mockMvc.perform(post("/movies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newMovieJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.title").value("New Movie"));
	}

	@Test
	public void testUpdateMovie() throws Exception {
		String updateMovieJson = "{ \"title\": \"Updated Movie Title\", \"genre\": \"Action\", \"duration\": 130, \"rating\": 9.0, \"releaseYear\": 2026 }";

		mockMvc.perform(post("/movies/update/Movie1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateMovieJson))
				.andExpect(status().isOk());

		mockMvc.perform(get("/movies/all"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Updated Movie Title"));
	}

	@Test
	public void testDeleteMovie() throws Exception {
		// Perform delete operation for Movie2
		mockMvc.perform(delete("/movies/Movie2"))
				.andExpect(status().isOk());

		// Verify that Movie1 still exists
		mockMvc.perform(get("/movies/all"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].title").value("Movie1"));

		// Verify that Movie2 is deleted
		mockMvc.perform(get("/movies/Movie2"))
				.andExpect(status().isBadRequest());  // Should return 404 if Movie2 is deleted
	}

	// ShowTimeController Tests
	@Test
	public void testGetShowtimeById() throws Exception {
		ShowTime showTime = showTimeRepository.findAll().get(0);

		mockMvc.perform(get("/showtimes/{showtimeId}", showTime.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(showTime.getId()))
				.andExpect(jsonPath("$.theater").value("Sample Theater"));
	}

	@Test
	public void testAddShowtime() throws Exception {
		Movie movie = movieRepository.findAll().get(0);
		Long movieId = movie.getId();
		String newShowtimeJson = String.format(
				"{ \"movieId\": %d, \"price\": 20.2, \"theater\": \"Sample Theater\", \"startTime\": \"2025-02-14T11:47:46.125405\", \"endTime\": \"2025-02-14T14:47:46.125405\" }",
				movieId
		);
		mockMvc.perform(post("/showtimes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newShowtimeJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.price").value(20.2));
	}

	@Test
	public void testUpdateShowtime() throws Exception {
		ShowTime showTime = showTimeRepository.findAll().get(0);
		Long movieId = showTime.getMovie().getId();
		String updateShowtimeJson = String.format(
				"{ \"movieId\": %d, \"price\": 30.0, \"theater\": \"Updated Theater\", \"startTime\": \"2025-02-14T12:47:46.125405Z\", \"endTime\": \"2025-02-14T15:47:46.125405Z\" }",
				movieId
		);
		mockMvc.perform(post("/showtimes/update/{showtimeId}", showTime.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateShowtimeJson))
				.andExpect(status().isOk());

		mockMvc.perform(get("/showtimes/{showtimeId}", showTime.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.theater").value("Updated Theater"));
	}

	@Test
	public void testDeleteShowtime() throws Exception {
		ShowTime showTime = showTimeRepository.findAll().get(0);

		mockMvc.perform(delete("/showtimes/{showtimeId}", showTime.getId()))
				.andExpect(status().isOk());

		mockMvc.perform(get("/showtimes/{showtimeId}", showTime.getId()))
				.andExpect(status().isBadRequest());
	}

	// BookingController Tests
	@Test
	public void testBookTicket() throws Exception {
		ShowTime showTime = showTimeRepository.findAll().get(0);
		String bookingJson = "{ \"showtimeId\": " + showTime.getId() + ", \"seatNumber\": 15, \"userId\": \"84438967-f68f-4fa0-b620-0f08217e76af\" }";

		mockMvc.perform(post("/bookings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookingJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.bookingId").isString());
	}
}
