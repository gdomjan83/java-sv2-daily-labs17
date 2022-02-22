package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieRepository {
    private DataSource dataSource;

    public MovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int saveMovie(String title, LocalDate releaseDate) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement stmt = conn.prepareStatement("insert into movies (movie_name, release_date, avg_rating) values (?, ?, 0.0)",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();
            return getMovieId(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not update.", sqle);
        }
    }

    public void updateAverageRating(double averageRating, int movieId) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
        PreparedStatement stmt = conn.prepareStatement("UPDATE movies SET avg_rating = ? WHERE id = ?")) {
            stmt.setDouble(1, averageRating);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update average rating.", sqle);
        }
    }

    private int getMovieId(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new IllegalStateException("Cannot return id.");
        }
    }

    public Optional<Movie> findMovieByTitle(String title) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement stmt = conn.prepareStatement("select * from movies where movie_name = ?")) {
            stmt.setString(1, title);
            return runQuery(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not find movie.", sqle);
        }
    }

    private Optional<Movie> runQuery(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Movie movie = new Movie(rs.getLong("id"), rs.getString("movie_name"),
                        rs.getDate("release_date").toLocalDate());
                return Optional.of(movie);
            }
            return Optional.empty();
        }
    }

    public List<Movie> findAllMovies() {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from movies")) {

            return collectMoviesToList(rs);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not find movies.", sqle);
        }
    }

    private List<Movie> collectMoviesToList(ResultSet rs) throws SQLException {
        List<Movie> result = new ArrayList<>();
        while (rs.next()) {
            Movie movie = new Movie(
                    (long) rs.getInt("id"),
                    rs.getString("movie_name"),
                    rs.getDate("release_Date").toLocalDate());
            result.add(movie);
        }
        return result;
    }
}
