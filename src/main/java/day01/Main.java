package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

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

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        actorsRepository.saveActor("Bodnár Gabriella");

    }
}
