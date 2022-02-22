package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public double getAverageRatingForMovie(int movieId) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT CAST(AVG(rating) as DECIMAL(10,2)) as avg FROM ratings WHERE movie_id = ?")) {
            stmt.setInt(1, movieId);
            return getAverageRatingFromTable(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not find average rating.", sqle);
        }
    }

    private double getAverageRatingFromTable(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("avg");
            }
            throw new IllegalStateException("Can not find average rating.");
        }
    }

    private void runUpdate(Connection conn, List<Integer> ratings, int movie_id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                //language=sql
                "insert into ratings(movie_id, rating) values (?, ?)")) {
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
