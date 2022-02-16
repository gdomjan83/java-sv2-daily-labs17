package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActorWithoutParameter() {
        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {
            //language=sql
            stmt.executeUpdate("insert into actors(actor_name) values('Kandó Kata')");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot save to database.", sqle);
        }
    }

    public void saveActor(String actor) {
        try (Connection connection = dataSource.getConnection();
            //language=sql
            PreparedStatement stmt = connection.prepareStatement("insert into actors(actor_name) values(?)")) {

            stmt.setString(1, actor);
            stmt.execute();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot save to database.", sqle);
        }
    }
}
