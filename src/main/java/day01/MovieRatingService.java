package day01;

import java.util.Arrays;
import java.util.Optional;

public class MovieRatingService {
    private MovieRepository movieRepository;
    private RatingsRepository ratingsRepository;

    public MovieRatingService(MovieRepository movieRepository, RatingsRepository ratingsRepository) {
        this.movieRepository = movieRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> movieFound = movieRepository.findMovieByTitle(title);
        if (movieFound.isPresent()) {
            ratingsRepository.rateMovie(movieFound.get().getId().intValue(), Arrays.asList(ratings));
        } else {
            throw new IllegalArgumentException("Can not find movie.");
        }
    }
}
