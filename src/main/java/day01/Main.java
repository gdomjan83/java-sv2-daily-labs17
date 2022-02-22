package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies_actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root456");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect.", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MovieRepository movieRepository = new MovieRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        RatingsRepository ratingsRepository = new RatingsRepository(dataSource);

        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, movieRepository, actorsMoviesRepository);
        MovieRatingService movieRatingService = new MovieRatingService(movieRepository, ratingsRepository);

        actorsMoviesService.insertMovieWithActors("Titanic", LocalDate.of(1997, 12 ,19),
                List.of("Leonardo DiCaprio", "Kate Blanchet", "Billy Zane"));
        actorsMoviesService.insertMovieWithActors("Terminator", LocalDate.of(1988, 5,26),
                List.of("Arnold Schwarzenegger", "Linda Hamilton", "Michael Biehn"));
        actorsMoviesService.insertMovieWithActors("The Wolf of Wall Street", LocalDate.of(2013, 12,25),
                List.of("Leonardo DiCaprio", "Jonah Hill"));

        movieRatingService.addRatings("Terminator", 1, 3, 2, 4, 5, 5);
        movieRatingService.addRatings("Titanic", 1, 3, 2, 4, 3, 5);
        movieRatingService.addRatings("The Wolf of Wall Street", 5, 5, 4, 4);
    }
}
