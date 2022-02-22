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
            int movieId = movieFound.get().getId().intValue();
            ratingsRepository.rateMovie(movieId, Arrays.asList(ratings));
            updateAverageRating(movieId);
        } else {
            throw new IllegalArgumentException("Can not find movie.");
        }
    }

    private void updateAverageRating(int movieId) {
        double avgRating = ratingsRepository.getAverageRatingForMovie(movieId);
        movieRepository.updateAverageRating(avgRating, movieId);
    }
}
