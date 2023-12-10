import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModelGame {
    private ArrayList<ModelPlayer> players;
    private ArrayList<ModelLevel> levels;
    private ModelLevel currentLevel;
    private ModelTimer timer;
    private boolean gameInProgress;
    private int currentLevelIndex;
    private final int totalLevels = 10;
    private ModelStorage storage;

    public ModelGame() {
        initializeGameComponents();
        loadLevels();
    }

    private void initializeGameComponents() {
        players = new ArrayList<>();
        timer = new ModelTimer();
        gameInProgress = false;
        currentLevelIndex = 0;
        levels = new ArrayList<>();
        storage = new ModelStorage();
    }

    private void loadLevels() {
        for (int i = 1; i <= totalLevels; i++) {
            levels.add(new ModelLevel(i, 600 + i * 20, 600 + i * 20));
        }
    }

    public void startGame() {
        if(!gameInProgress) {
            gameInProgress = true;
            currentLevelIndex = 0;
            setCurrentLevel();
            timer.resetTimer();
            timer.startTimer();
        }
    }

    private void setCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            currentLevel = levels.get(currentLevelIndex);
            initialiseGame();
        } else {
            endGame();
        }
    }

    private void resetGameState() {
        // Reset the game state for a new level
        // This typically includes resetting player positions and light trails
        int initialY = 10;
        for (ModelPlayer player : players) {
            player.resetPlayer(800, initialY); // Reset each player's position
            initialY += 10;
        }
    }

    private void initialiseGame() {
        // Calculate the center of the game area
        currentLevel = getCurrentLevel();
        int centerX = currentLevel.getWidth() / 2;
        int centerY = currentLevel.getHeight() / 2;

        if (players.size() >= 2) {
            ModelPlayer player1 = players.get(0);
            ModelPlayer player2 = players.get(1);

            // Position player1 slightly to the left of the center, facing right
            player1.setXPosition(centerX - 20); // Adjust 20 to your game's scale
            player1.setYPosition(centerY);
            player1.move("RIGHT");

            // Position player2 slightly to the right of the center, facing left
            player2.setXPosition(centerX + 20); // Adjust 20 to your game's scale
            player2.setYPosition(centerY);
            player2.move("LEFT");
        }
    }

    public void movePlayer(int playerId, String direction) {
        //if (!gameInProgress) return;

        ModelPlayer player = players.get(playerId);
        player.move(direction);
        checkGameState();
    }

    public void checkGameState() {
        for (ModelPlayer player : players) {
            if (hasCollided(player)) {
                timer.stopTimer();
                String winnerName = determineWinner(player);
                updateWinnerScore(winnerName);
                advanceToNextLevel();
                break; // Exit the loop if a collision occurs
            }
        }
    }

    private boolean hasCollided(ModelPlayer player) {
        // Check for boundary collisions
        if (player.getXPosition() < 0 || player.getXPosition() >= currentLevel.getWidth() ||
                player.getYPosition() < 0 || player.getYPosition() >= currentLevel.getHeight()) {
            return true;
        }
        // Check for collisions with the player's own light trail
        if (intersects(player, player.getLightTrail())) { return true; }

        // Check for collisions with the opponent's light trail
        for (ModelPlayer otherPlayer : players) {
            if (otherPlayer != player && intersects(player, otherPlayer.getLightTrail())) return true;
        }
        return false; // No collision detected
    }

    private String determineWinner(ModelPlayer playerThatMoved) {
        // Check if the moving player collided with their own light trail
        if (intersects(playerThatMoved, playerThatMoved.getLightTrail())) {
            return getOpponentName(playerThatMoved);
        }

        // Check if the moving player collided with the opponent's light trail
        for (ModelPlayer player : players) {
            if (player != playerThatMoved && intersects(playerThatMoved, player.getLightTrail())) {
                return getOpponentName(playerThatMoved);
            }
        }

        // Check boundary collisions
        if (isOutOfBounds(playerThatMoved)) { return getOpponentName(playerThatMoved); }
        return null; // In case of a tie or other scenarios
    }

    private boolean isOutOfBounds(ModelPlayer player) {
        return player.getXPosition() < 0 || player.getXPosition() >= currentLevel.getWidth() ||
                player.getYPosition() < 0 || player.getYPosition() >= currentLevel.getHeight();
    }

    private String getOpponentName(ModelPlayer player) {
        for (ModelPlayer opponent : players) { if (opponent != player) { return opponent.getName(); } }
        return null;
    }

    private void updateWinnerScore(String winnerName) {
        for (ModelPlayer player : players) {
            if (player.getName().equals(winnerName)) {
                int newScore = player.getScore() + 1;
                player.setScore(newScore);
                storage.updateScore(player.getName(), newScore); // Update cumulative score in the database
            }
        }
    }

    private void advanceToNextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex < totalLevels) {
            setCurrentLevel();
            timer.startTimer();
        } else {
            displayFinalScores();
            displayHighScores();
            restartGame();
        }
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void endGame() {
        gameInProgress = false;
        timer.stopTimer(); // Ensure the timer stops when the game ends
        displayFinalScores();
        displayHighScores(); // Display top 10 high scores from the database
        restartGame();
    }

    private void displayFinalScores() {
        System.out.println("Final Scores:");
        for (ModelPlayer player : players) {
            System.out.println(player.getName() + " Score: " + player.getScore());
        }
    }

    private void displayHighScores() {
        System.out.println("High Scores:");
        List<String> highScores = storage.getHighScore();
        for (String score : highScores) {
            System.out.println(score);
        }
    }

    private void restartGame() {
        currentLevelIndex = 0;
        setCurrentLevel();
        //resetGameState();
        timer.resetTimer(); // Timer is reset but players' scores are not reset
    }

    private boolean intersects(ModelPlayer player, List<Point> lightTrail) {
        Point playerPosition = new Point(player.getXPosition(), player.getYPosition());
        return lightTrail.contains(playerPosition);
    }

    public void initializePlayers(String name1, Color colour1, String name2, Color colour2) {
        players.add(new ModelPlayer(name1, colour1));
        players.add(new ModelPlayer(name2, colour2));
    }

    // Getters and setters...
    public ModelLevel getCurrentLevel() { return currentLevel; }
    public ModelTimer getTimer() { return timer; }
    public List<ModelPlayer> getPlayers() { return players; }
}
