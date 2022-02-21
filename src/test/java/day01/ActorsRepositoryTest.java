package day01;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ActorsRepositoryTest {
    private MariaDbDataSource dataSource;
    private ActorsRepository actorsRepository;
    private MovieRepository movieRepository;

    @BeforeEach
    void init() {
        dataSource = new MariaDbDataSource();
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

        actorsRepository = new ActorsRepository(dataSource);
        movieRepository = new MovieRepository(dataSource);
    }

    @Test
    void saveActorTest() {
        actorsRepository.saveActor("Leonardo DiCaprio");
        actorsRepository.saveActor("Arnold Schwarzenegger");
        actorsRepository.saveActor("Tom Holland");
        List<String> result = actorsRepository.listActorsWithoutParameter();
        assertEquals(3, result.size());
    }

    @Test
    void findActorByNameTest() {
        actorsRepository.saveActor("Leonardo DiCaprio");
        actorsRepository.saveActor("Arnold Schwarzenegger");
        actorsRepository.saveActor("Tom Holland");
        Optional<Actor> result = actorsRepository.findActorByName("Tom Holland");
        assertEquals("Tom Holland", result.get().getActorName());
    }
}