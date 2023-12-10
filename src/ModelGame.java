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
    public ModelStorage modelstorage;
    private ControllerGame controllergame;

    public ModelGame(ControllerGame controllergame) {
        initializeGameComponents();
        loadLevels();
        this.controllergame = controllergame;
        //this.scoreUpdatedForLevel = false;
    }

    private void initializeGameComponents() {
        this.players = new ArrayList<>();
        this.timer = new ModelTimer();
        this.gameInProgress = false;
        this.currentLevelIndex = 0;
        this.levels = new ArrayList<>();
        this.modelstorage = new ModelStorage();
    }

    private void loadLevels() {
        for (int i = 1; i <= totalLevels; i++) {
            levels.add(new ModelLevel(i, 600 - i * 20, 600 - i * 20));
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
        } else { endGame(); }
    }

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
    }

    public boolean checkGameState() {
        for (ModelPlayer player : players) { if (hasCollided(player)) return true; }
        return false;
    }

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
        return false;
    }

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
        return null;
    }

    private boolean isOutOfBounds(ModelPlayer player) {
        return player.getXPosition() < 0 || player.getXPosition() >= currentLevel.getWidth() ||
                player.getYPosition() < 0 || player.getYPosition() >= currentLevel.getHeight();
    }

    private String getOpponentName(ModelPlayer player) {
        for (ModelPlayer opponent : players) { if (opponent != player) return opponent.getName(); }
        return null;
    }

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

    public void advanceToNextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex < totalLevels) {
            setCurrentLevel();
            timer.resetTimer();
            timer.startTimer();
        } else controllergame.endGameAndShowScores();
    }

    public void endGame() { gameInProgress = false; timer.stopTimer(); }

    public void restartGame() {
        currentLevelIndex = 0;
        setCurrentLevel();
        for (ModelPlayer player : players) player.setScore(0);
        timer.resetTimer();
        gameInProgress = false;
    }

    private boolean intersects(ModelPlayer player, List<Point> lightTrail) {
        if (lightTrail.isEmpty()) return false;

        Point playerPosition = new Point(player.getXPosition(), player.getYPosition());

        for (int i = 0; i < lightTrail.size() - 1; i++) { if (lightTrail.get(i).equals(playerPosition)) return true; }
        return false;
    }

    public void initializePlayers(String name1, Color colour1, String name2, Color colour2) {
        players.add(new ModelPlayer(name1, colour1));
        players.add(new ModelPlayer(name2, colour2));
    }

    public ModelLevel getCurrentLevel() { return currentLevel; }
    public ModelTimer getTimer() { return timer; }
    public List<ModelPlayer> getPlayers() { return players; }
    public int getCurrentLevelIndex() { return currentLevelIndex; }
}
