package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActorsMoviesRepository {
    private DataSource dataSource;

    public ActorsMoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActorAndMovieId(int actorId, int movieId) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement stmt = conn.prepareStatement("insert into actors_movies(actor_id, movie_id) values (?, ?)")) {
            stmt.setInt(1, actorId);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert rows.", sqle);
        }
    }
}
