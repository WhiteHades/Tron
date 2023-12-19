import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ModelGame class
 */
public class ModelGame {
    private ArrayList<ModelPlayer> players;
    private ArrayList<ModelLevel> levels;
    private ModelLevel currentLevel;
    private ModelTimer timer;
    private boolean gameInProgress;
    private int currentLevelIndex;
    private final int totalLevels = 10;
    public ModelStorage modelstorage;
    private ControllerGame controllergame;

    public ModelGame(ControllerGame controllergame) {
        initializeGameComponents();
        loadLevels();
        this.controllergame = controllergame;
        //this.scoreUpdatedForLevel = false;
    }

    /**
     * Initializes the game components
     */
    private void initializeGameComponents() {
        this.players = new ArrayList<>();
        this.timer = new ModelTimer();
        this.gameInProgress = false;
        this.currentLevelIndex = 0;
        this.levels = new ArrayList<>();
        this.modelstorage = new ModelStorage();
    }

    /**
     * Loads the levels
     */
    private void loadLevels() {
        for (int i = 1; i <= totalLevels; i++) {
            levels.add(new ModelLevel(i, 600, 600));
        }
    }

    /**
     * Starts the game
     */
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
        } else { endGame(); }
    }

    /**
     * Initialises the game
     */
    private void initialiseGame() {
        for (ModelPlayer player : players) { player.resetLightTrail(); }

        currentLevel = getCurrentLevel();
        int centerX = currentLevel.getWidth() / 2;
        int centerY = currentLevel.getHeight() / 2;

        if (players.size() >= 2) {
            ModelPlayer player1 = players.get(0);
            ModelPlayer player2 = players.get(1);

            player1.setXPosition(centerX - 20);
            player1.setYPosition(centerY);
            player1.move("LEFT");

            player2.setXPosition(centerX + 20);
            player2.setYPosition(centerY);
            player2.move("RIGHT");
        }

        currentLevel.getObstacles().clear();

        int numofobstalces = currentLevelIndex + 1;

        for(int i = 0; i<numofobstalces; i++) {
            int margin = 10;
            int x = (int) (Math.random() * (currentLevel.getWidth()-margin));
            int y = (int) (Math.random() * (currentLevel.getHeight()-margin));
            currentLevel.getObstacles().add(new Point(x, y));
        }
    }

    /**
     * Checks if the player has collided or not
     */
    public boolean checkGameState() {
        for (ModelPlayer player : players) { if (hasCollided(player)) return true; }
        return false;
    }

    private boolean collidedWithObstacles(ModelPlayer player) {
        int obstacleheight = 12;
        int obstaclewidth = 12;

        Rectangle playerRectangle = new Rectangle(player.getXPosition(), player.getYPosition(), obstaclewidth, obstacleheight);
        for (Point obstacle : currentLevel.getObstacles()) {
            Rectangle obstacleRect = new Rectangle(obstacle.x, obstacle.y, obstacleheight, obstaclewidth);
            if (playerRectangle.intersects(obstacleRect)) return true;
        } return false;
    }

    /**
     * Checks if the player has collided with either the boundary or a light trail (either their own or the opponent's)
     */
    private boolean hasCollided(ModelPlayer player) {
        // Check for boundary collisions
        if (player.getXPosition() < 0 || player.getXPosition() >= currentLevel.getWidth() ||
                player.getYPosition() < 0 || player.getYPosition() >= currentLevel.getHeight()) return true;

        // Check for collisions with the player's own light trail
        if (intersects(player, player.getLightTrail())) return true;

        // Check for collisions with the opponent's light trail
        for (ModelPlayer otherPlayer : players) {
            if (otherPlayer != player && intersects(player, otherPlayer.getLightTrail())) return true;
        }

        return collidedWithObstacles(player);
    }

    /**
     * Determines the winner of the game
     * @param playerThatMoved
     * @return
     */
    public String determineWinner(ModelPlayer playerThatMoved) {
        // Check if the moving player collided with their own light trail
        if (intersects(playerThatMoved, playerThatMoved.getLightTrail())) return getOpponentName(playerThatMoved);

        // Check if the moving player collided with the opponent's light trail
        for (ModelPlayer player : players) {
            if (player != playerThatMoved && intersects(playerThatMoved, player.getLightTrail())) {
                return getOpponentName(playerThatMoved);
            }
        }

        // Check boundary collisions
        if (isOutOfBounds(playerThatMoved)) { return getOpponentName(playerThatMoved); }
        if(collidedWithObstacles(playerThatMoved)) { return getOpponentName(playerThatMoved); }

        return null;
    }

    /**
     * Checks if the player is out of bounds
     */
    private boolean isOutOfBounds(ModelPlayer player) {
        return player.getXPosition() < 0 || player.getXPosition() >= currentLevel.getWidth() ||
                player.getYPosition() < 0 || player.getYPosition() >= currentLevel.getHeight();
    }

    /**
     * Gets the opponent's name
     */
    private String getOpponentName(ModelPlayer player) {
        for (ModelPlayer opponent : players) { if (opponent != player) return opponent.getName(); }
        return null;
    }

    /**
     * Updates the winner's score
     */
    public void updateWinnerScore(String winnerName) {
        for (ModelPlayer player : players) {
            if (player.getName().equals(winnerName)) {
                int newScore = player.getScore() + 1;
                player.setScore(newScore);
                System.out.println("Updating score for " + winnerName + " to " + newScore);
                modelstorage.updateScore(player.getName(), newScore);
            }
        }
    }

    /**
     * Advances to the next level
     */
    public void advanceToNextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex < totalLevels) {
            setCurrentLevel();
            timer.resetTimer();
            timer.startTimer();
        } else controllergame.endGameAndShowScores();
    }

    /**
     * Ends the game
     */
    public void endGame() { gameInProgress = false; timer.stopTimer(); }

    /**
     * Restarts the game
     */
    public void restartGame() {
        currentLevelIndex = 0;
        setCurrentLevel();
        for (ModelPlayer player : players) player.setScore(0);
        timer.resetTimer();
        gameInProgress = false;
    }

    /**
     * Checks if the player intersects with the light trail
     */
    private boolean intersects(ModelPlayer player, List<Point> lightTrail) {
        if (lightTrail.isEmpty()) return false;

        Point playerPosition = new Point(player.getXPosition(), player.getYPosition());

        for (int i = 0; i < lightTrail.size() - 1; i++) { if (lightTrail.get(i).equals(playerPosition)) return true; }
        return false;
    }

    /**
     * Initializes the players
     */
    public void initializePlayers(String name1, Color colour1, String name2, Color colour2) {
        players.add(new ModelPlayer(name1, colour1));
        players.add(new ModelPlayer(name2, colour2));
    }

    public ModelLevel getCurrentLevel() { return currentLevel; }
    public ModelTimer getTimer() { return timer; }
    public List<ModelPlayer> getPlayers() { return players; }
    public int getCurrentLevelIndex() { return currentLevelIndex; }
}
