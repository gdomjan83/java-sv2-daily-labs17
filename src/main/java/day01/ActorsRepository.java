package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}
