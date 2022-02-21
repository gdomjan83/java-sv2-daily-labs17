package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RatingsRepository {
    private DataSource dataSource;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void rateMovie(int movie_id, List<Integer> ratings) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            runUpdate(conn, ratings, movie_id);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not insert rating.", sqle);
        }
    }

    private void runUpdate(Connection conn, List<Integer> ratings, int movie_id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("insert into ratings(movie_id, rating) values (?, ?)")) {
            validateAndSetValues(ratings, stmt, movie_id);
            conn.commit();
        } catch (IllegalArgumentException iae) {
            conn.rollback();
        }
    }

    private void validateAndSetValues(List<Integer> ratings, PreparedStatement stmt, int movie_id) throws SQLException {
        for (Integer actual : ratings) {
            if (actual < 1 || actual > 5) {
                throw new IllegalArgumentException("Rating is invalid.");
            }
            stmt.setInt(1, movie_id);
            stmt.setInt(2, actual);
            stmt.executeUpdate();
        }
    }
}
