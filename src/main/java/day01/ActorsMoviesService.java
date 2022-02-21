package day01;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActorsMoviesService {
    private ActorsRepository actorsRepository;
    private MovieRepository movieRepository;
    private ActorsMoviesRepository actorsMoviesRepository;

    public ActorsMoviesService(ActorsRepository actorsRepository, MovieRepository movieRepository, ActorsMoviesRepository actorsMoviesRepository) {
        this.actorsRepository = actorsRepository;
        this.movieRepository = movieRepository;
        this.actorsMoviesRepository = actorsMoviesRepository;
    }

    public void insertMovieWithActors(String title, LocalDate releaseDate, List<String> actors) {
        int movieId = movieRepository.saveMovie(title, releaseDate);
        int actorId;
        for (String actual : actors) {
            Optional<Actor> foundActor = actorsRepository.findActorByName(actual);
            if (foundActor.isPresent()) {
                actorId = foundActor.get().getId();
            } else {
                actorId = actorsRepository.saveActor(actual);
            }
            actorsMoviesRepository.insertActorAndMovieId(actorId, movieId);
        }
    }
}
