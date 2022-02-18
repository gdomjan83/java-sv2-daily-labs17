package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private DataSource dataSource;

    public MovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate releaseDate) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement stmt = conn.prepareStatement("insert into movies (movie_name, release_date) values (?, ?)")) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not update.", sqle);
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
