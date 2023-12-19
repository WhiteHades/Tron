import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.awt.event.KeyEvent;

/**
 * Constructs a ControllerGame, initializes the game model and view, sets up player controls, and starts the game loop.
 */
public class ControllerGame{
    private boolean running;
    public ViewGame viewgame;
    public ModelGame modelgame;
    private Timer gameLoopTimer;
    private Map<Integer, Boolean> keyStates;
    private Map<Integer, String> player1Controls;
    private Map<Integer, String> player2Controls;
    private boolean isGamePaused = false;

    public ControllerGame() {
        this.modelgame = new ModelGame(this);
        this.keyStates = new HashMap<>();
        this.viewgame = new ViewGame(this);

        initializePlayerControls();
        startGameLoopThread();
    }

    /**
     * Starts the game loop thread.
     */
    private void startGameLoopThread() {
        gameLoopTimer = new Timer(35, e -> updateGame());
        running = true;
        gameLoopTimer.start();
    }

    public void pauseGame(boolean pause) { isGamePaused  = pause; }

    /**
     * Updates the game state and view.
     */
    private void updateGame() {
        // updatePlayerPositions();
        // Update player positions and game state
        if(!isGamePaused) {
            for (ModelPlayer player : modelgame.getPlayers()) {
                player.move(player.getDirection());
                boolean collisionDetected = modelgame.checkGameState();
                if (collisionDetected) {
                    String winnerName = modelgame.determineWinner(player); // Implement this method
                    for (ModelPlayer player2 : modelgame.getPlayers()) {
                        if (player2.getName().equals(winnerName)) { player2.setScore((player2.getScore() + 1)); }
                    }
                    viewgame.showWinnerDialog(winnerName);
                    modelgame.advanceToNextLevel();
                    viewgame.initialiseGamePanel();
                    viewgame.updateCurrentLevelDisplay();
                } viewgame.updateGame();
            }
        }
    }

    /**
     * Ends the game and shows the scores.
     */
    public void endGameAndShowScores() {
        for (ModelPlayer player : modelgame.getPlayers()) {
            String winnerName = modelgame.determineWinner(player);
            modelgame.updateWinnerScore(winnerName);
        }
        List<String> highScores = modelgame.modelstorage.getHighScore();
        StringBuilder scoresMessage = new StringBuilder();

        scoresMessage.append("Current Game Score:\n");
        for (ModelPlayer player : modelgame.getPlayers()) {
            scoresMessage.append(player.getName()).append(": ").append(player.getScore()).append("\n");
        }

        scoresMessage.append("\nOverall Game Score:\n");
        for (String score : highScores) scoresMessage.append(score).append("\n");

        scoresMessage.append("\nRestart game?");

        int n = JOptionPane.showOptionDialog(null, scoresMessage.toString(), "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{"Yes", "No"}, "Yes");

        if (n == JOptionPane.YES_OPTION) restartGame();
        else System.exit(0);
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
        modelgame.restartGame();
        viewgame.updateCurrentLevelDisplay();
        viewgame.initialiseGamePanel();
        startGame(); //
    }

    /**
     * Initializes the player controls.
     */
    private void initializePlayerControls() {
        player1Controls = new HashMap<>();
        player2Controls = new HashMap<>();

        // Player 1 controls (WASD)
        player1Controls.put(KeyEvent.VK_W, "UP");
        player1Controls.put(KeyEvent.VK_S, "DOWN");
        player1Controls.put(KeyEvent.VK_A, "LEFT");
        player1Controls.put(KeyEvent.VK_D, "RIGHT");

        // Player 2 controls (Arrow keys)
        player2Controls.put(KeyEvent.VK_UP, "UP");
        player2Controls.put(KeyEvent.VK_DOWN, "DOWN");
        player2Controls.put(KeyEvent.VK_LEFT, "LEFT");
        player2Controls.put(KeyEvent.VK_RIGHT, "RIGHT");
    }

    /**
     * Initializes the players.
     * @param name1 Player 1 name
     * @param color1 Player 1 color
     * @param name2 Player 2 name
     * @param color2 Player 2 color
     */
    public void initializePlayers(String name1, Color color1, String name2, Color color2) {
        modelgame.initializePlayers(name1, color1, name2, color2);
    }

    /**
     * Handles player action events.
     * @param e KeyEvent
     * @param isPressed Whether the key is pressed or released
     */
    public void handlePlayerAction(KeyEvent e, boolean isPressed) {
        int keyCode = e.getKeyCode();

        if (player1Controls.containsKey(keyCode)) {
            if (isPressed) modelgame.getPlayers().get(0).setDirection(player1Controls.get(keyCode));
        } else if (player2Controls.containsKey(keyCode)) {
            if (isPressed) modelgame.getPlayers().get(1).setDirection(player2Controls.get(keyCode));
        }
    }

    /**
     * Starts the game.
     */
    public void startGame() {
        modelgame.startGame();
        viewgame.initialiseGamePanel();
        viewgame.initialiseMenuBar();
        viewgame.updateGame();
        viewgame.getGamePanel().setVisible(true);
        viewgame.getGamePanel().requestFocusInWindow();
    }

    public Dimension getCurrentLevelSize() {
        ModelLevel currentLevel = modelgame.getCurrentLevel();
        return new Dimension(currentLevel.getWidth(), currentLevel.getHeight());
    }

    public ModelGame getGameModel() { return modelgame; }
}