import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.awt.event.KeyEvent;

public class ControllerGame{
    private boolean running;
    public ViewGame viewgame;
    public ModelGame modelgame;
    private Timer gameLoopTimer;
    private Map<Integer, Boolean> keyStates;
    private Map<Integer, String> player1Controls;
    private Map<Integer, String> player2Controls;

    public ControllerGame() {
        this.modelgame = new ModelGame(this);
        this.keyStates = new HashMap<>();
        this.viewgame = new ViewGame(this);

        initializePlayerControls();
        startGameLoopThread();
    }

    // Starting the game loop thread
    private void startGameLoopThread() {
        gameLoopTimer = new Timer(100, e -> updateGame());
        running = true;
        gameLoopTimer.start();
    }

    private void updateGame() {
        // updatePlayerPositions();
        // Update player positions and game state
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

    public void endGameAndShowScores() {
        for (ModelPlayer player : modelgame.getPlayers()) {
            String winnerName = modelgame.determineWinner(player);
            modelgame.updateWinnerScore(winnerName);
        }
        List<String> highScores = modelgame.modelstorage.getHighScore();
        StringBuilder scoresMessage = new StringBuilder();
        scoresMessage.append("Final Scores:\n");

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

    private void restartGame() {
        modelgame.restartGame();
        viewgame.updateCurrentLevelDisplay();
        viewgame.initialiseGamePanel();
        startGame(); //
    }

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

    public void initializePlayers(String name1, Color color1, String name2, Color color2) {
        modelgame.initializePlayers(name1, color1, name2, color2);
    }

    public void handlePlayerAction(KeyEvent e, boolean isPressed) {
        int keyCode = e.getKeyCode();

        if (player1Controls.containsKey(keyCode)) {
            if (isPressed) modelgame.getPlayers().get(0).setDirection(player1Controls.get(keyCode));
        } else if (player2Controls.containsKey(keyCode)) {
            if (isPressed) modelgame.getPlayers().get(1).setDirection(player2Controls.get(keyCode));
        }
    }

    private void updatePlayerPositions() {
        if (modelgame.getPlayers().size() < 2) { return; }

        // Player 1 movement
        updatePlayerMovement(0, KeyEvent.VK_W, "UP");
        updatePlayerMovement(0, KeyEvent.VK_S, "DOWN");
        updatePlayerMovement(0, KeyEvent.VK_A, "LEFT");
        updatePlayerMovement(0, KeyEvent.VK_D, "RIGHT");

        // Player 2 movement
        updatePlayerMovement(1, KeyEvent.VK_UP, "UP");
        updatePlayerMovement(1, KeyEvent.VK_DOWN, "DOWN");
        updatePlayerMovement(1, KeyEvent.VK_LEFT, "LEFT");
        updatePlayerMovement(1, KeyEvent.VK_RIGHT, "RIGHT");
    }

    private void updatePlayerMovement(int playerId, int keyCode, String direction) {
        if (keyStates.getOrDefault(keyCode, false)) modelgame.getPlayers().get(playerId).move(direction);
    }

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
