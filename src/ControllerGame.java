import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.KeyEvent;

public class ControllerGame {
    private boolean running;
    public ViewGame viewgame;
    private ModelGame modelgame;
    private Timer gameLoopTimer;
    private Map<Integer, Boolean> keyStates;
    private ControllerStorage controllerstorage;

    public ControllerGame() {
        this.modelgame = new ModelGame();
        this.keyStates = new HashMap<>();
        this.controllerstorage = new ControllerStorage();
        this.viewgame = new ViewGame(this, controllerstorage);

        initializeKeyStates();
        startGameLoopThread();
    }

    // Starting the game loop thread
    private void startGameLoopThread() {
        gameLoopTimer = new Timer(25, e -> updateGame());
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
                viewgame.showWinnerDialog(winnerName);
                viewgame.initialiseGamePanel();
                modelgame.advanceToNextLevel();
            }
            viewgame.updateGame();
        }
    }

    public void stopGame() {
        running = false;
        if (gameLoopTimer != null) {
            gameLoopTimer.stop();
        }
    }

    private void initializeKeyStates() {
        keyStates.put(KeyEvent.VK_W, false);
        keyStates.put(KeyEvent.VK_A, false);
        keyStates.put(KeyEvent.VK_S, false);
        keyStates.put(KeyEvent.VK_D, false);
        keyStates.put(KeyEvent.VK_UP, false);
        keyStates.put(KeyEvent.VK_LEFT, false);
        keyStates.put(KeyEvent.VK_DOWN, false);
        keyStates.put(KeyEvent.VK_RIGHT, false);
    }

    public void initializePlayers(String name1, Color color1, String name2, Color color2) {
        modelgame.initializePlayers(name1, color1, name2, color2);
    }

    public void handlePlayerAction(KeyEvent e, boolean isPressed) {
        // Update key state
        keyStates.put(e.getKeyCode(), isPressed);
        //if (isPressed) {keyStates.put(e.getKeyCode(), isPressed);}

        if(isPressed) {
            System.out.println("Key Pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
        }

        // Update player positions based on current key states
        updatePlayerPositions();
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
        if (keyStates.getOrDefault(keyCode, false)) {
            modelgame.getPlayers().get(playerId).move(direction);
        }
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
