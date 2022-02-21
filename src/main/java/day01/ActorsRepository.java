package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public int saveActor(String actor) {
        try (Connection connection = dataSource.getConnection();
            //language=sql
            PreparedStatement stmt = connection.prepareStatement(
                    "insert into actors(actor_name) values(?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, actor);
            stmt.executeUpdate();
            return returnGeneratedKey(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot save to database.", sqle);
        }
    }

    private int returnGeneratedKey(Statement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new IllegalStateException("No id generated.");
        }
    }

    public List<String> listActorsWithoutParameter() {
        List<String> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            //language=sql
            ResultSet rs = stmt.executeQuery("select actor_name from actors")) {
            while (rs.next()) {
                result.add(rs.getString("actor_name"));
            }
            return result;
        } catch (SQLException sqle) {
            throw new IllegalStateException("No connection.", sqle);
        }
    }

    public List<String> listActorsContainingSubstring(String substring) {
        List<String> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             //language=sql
        PreparedStatement stmt = conn.prepareStatement("select actor_name from actors where actor_name like ?")) {
            stmt.setString(1, "%" + substring + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("actor_name"));
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("No connection.", sqle);
        }
        return result;
    }

    public Optional<Actor> findActorByName(String name) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement stmt = conn.prepareStatement("select * from actors where actor_name = ?")) {
            stmt.setString(1, name);
            return actorSearched(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("No connection.", sqle);
        }
    }

    private Optional<Actor> actorSearched(PreparedStatement stmt) throws SQLException{
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Actor(rs.getInt("id"), rs.getString("actor_name")));
            }
            return Optional.empty();
        }
    }
}
