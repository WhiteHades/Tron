import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class modelStorage {
    private final String dbUrl = "jdbc:mysql://localhost/tron";
    private final String dbUser = "root";
    private final String dbPassword = "powerrangersRED89";

    public void updateScore(String name, int score) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO highscores (player_name, score) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE score = GREATEST(score, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, score);
                pstmt.setInt(3, score);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHighScore() {
        List<String> highscores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT player_name, score FROM highscores ORDER BY score DESC LIMIT 10";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) { highscores.add(rs.getString("player_name") + ": " + rs.getInt("score")); }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highscores;
    }
}
