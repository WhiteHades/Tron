import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class modelGame {
    private ArrayList<modelPlayer> players;
    private ArrayList<modelLevel> levels;
    private modelLevel currentLevel;
    private modelTimer timer;
    private boolean gameInProgress;
    private int currentLevelIndex;
    private final int totalLevels = 10;
    private modelStorage storage;

    public modelGame() {
        initializeGameComponents();
        loadLevels();
    }

    private void initializeGameComponents() {
        players = new ArrayList<>();
        timer = new modelTimer();
        gameInProgress = false;
        currentLevelIndex = 0;
        levels = new ArrayList<>();
        storage = new modelStorage();
    }

    private void loadLevels() {
        for (int i = 1; i <= totalLevels; i++) {
            levels.add(new modelLevel(i, 400 + i * 20, 400 + i * 20));
        }
    }

    public void startGame() {
        gameInProgress = true;
        currentLevelIndex = 0;
        setCurrentLevel();
        timer.startTimer();
    }

    private void setCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            currentLevel = levels.get(currentLevelIndex);
            initializeGame();
        } else {
            endGame();
        }
    }

    private void resetGameState() {
        // Reset the game state for a new level
        // This typically includes resetting player positions and light trails
        int initialY = 10;
        for (modelPlayer player : players) {
            player.resetPlayer(0, initialY); // Reset each player's position
            initialY += 10;
        }
    }

    private void initializeGame() {
        int initialY = 10;
        for (modelPlayer player : players) {
            player.move("RIGHT");
            player.setXPosition(0);
            player.setYPosition(initialY);
            initialY += 10;
        }
    }

    public void movePlayer(int playerId, String direction) {
        if (!gameInProgress) return;

        modelPlayer player = players.get(playerId);
        player.move(direction);
        checkGameState(player);
    }

    private void checkGameState(modelPlayer player) {
        if (hasCollided(player)) {
            timer.stopTimer();
            String winnerName = determineWinner(player);
            updateWinnerScore(winnerName);
            advanceToNextLevel();
        }
    }

    private boolean hasCollided(modelPlayer player) {
        return player.getXPosition() < 0 || player.getXPosition() >= currentLevel.getWidth() ||
                player.getYPosition() < 0 || player.getYPosition() >= currentLevel.getHeight() ||
                intersects(player, player.getLightTrail());
    }

    private String determineWinner(modelPlayer loser) {
        for (modelPlayer player : players) {
            if (player != loser) {
                return player.getName();
            }
        }
        return null; // In case of a tie or other scenarios
    }

    private void updateWinnerScore(String winnerName) {
        for (modelPlayer player : players) {
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

    public void endGame() {
        gameInProgress = false;
        displayFinalScores();
        displayHighScores(); // Display top 10 high scores from the database
        restartGame();
    }

    private void displayFinalScores() {
        System.out.println("Final Scores:");
        for (modelPlayer player : players) {
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
        resetGameState();
        timer.resetTimer(); // Timer is reset but players' scores are not reset
    }

    private boolean intersects(modelPlayer player, List<Point> lightTrail) {
        Point playerPosition = new Point(player.getXPosition(), player.getYPosition());
        return lightTrail.contains(playerPosition);
    }

    public void initializePlayers(String name1, Color colour1, String name2, Color colour2) {
        players.add(new modelPlayer(name1, colour1));
        players.add(new modelPlayer(name2, colour2));
    }

    // Getters and setters...
    public modelLevel getCurrentLevel() { return currentLevel; }
    public modelTimer getTimer() { return timer; }
    public List<modelPlayer> getPlayers() { return players; }
}
