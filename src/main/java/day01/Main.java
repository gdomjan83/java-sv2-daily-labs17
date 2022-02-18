package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;

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
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MovieRepository movieRepository = new MovieRepository(dataSource);
//        actorsRepository.saveActor("Leonardo DiCaprio");
//        movieRepository.saveMovie("Titanic", LocalDate.of(1997, 12 ,19));
//        movieRepository.saveMovie("Terminator", LocalDate.of(1988, 5,26));
        System.out.println(movieRepository.findAllMovies());
    }
}
