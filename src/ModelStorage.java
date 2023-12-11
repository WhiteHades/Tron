import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for storing and retrieving data from the database.
 */
public class ModelStorage {
    private final String dbUrl = "jdbc:mysql://localhost/tron";
    private final String dbUser = "root";
    private final String dbPassword = "powerrangersRED89";

    /**
     * This method is responsible for storing the player's name and score in the database.
     * @param name The player's name.
     * @param newPoints The player's score.
     */
    public void updateScore(String name, int newPoints) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO highscores (player_name, score) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE score = score + ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, newPoints);
                pstmt.setInt(3, newPoints);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * This method is responsible for retrieving the top 10 high scores from the database.
     * @return A list of the top 10 high scores.
     */
    public List<String> getHighScore() {
        List<String> highscores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT player_name, score FROM highscores ORDER BY score DESC LIMIT 10";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    highscores.add(rs.getString("player_name") + ": " + rs.getInt("score"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return highscores;
    }
}