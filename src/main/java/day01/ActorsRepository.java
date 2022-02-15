package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor() {
        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {
            //language=sql
            stmt.executeUpdate("insert into actors(actor_name) values('Kandó Kata')");

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot save to database.", sqle);
        }
    }
}
