import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.KeyEvent;

public class ControllerGame {
    private ViewGame viewgame;
    private ModelGame modelgame;
    private Timer gameLoopTimer;
    private Map<Integer, Boolean> keyStates;
    private ControllerStorage controllerstorage;

    public ControllerGame() {
        this.modelgame = new ModelGame();
        this.keyStates = new HashMap<>();
        this.controllerstorage = new ControllerStorage();
        this.viewgame = new ViewGame(this, controllerstorage);

        // Initialize key states
        initializeKeyStates();
        //startGameLoop();
    }

//    private void startGameLoop() {
//        gameLoopTimer = new Timer(100, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) { updateGame(); }
//        }); gameLoopTimer.start();
//    }

    private void updateGame() {
        updatePlayerPositions();
        modelgame.checkGameState();
        viewgame.updateGame();
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

//    public void updateGameView() {
//        int currentTime = modelgame.getTimer().getTime();
//        //viewgame.updateTimerDisplay(currentTime);
//        viewgame.updateGame();
//    }

    public void handlePlayerAction(KeyEvent e, boolean isPressed) {
//        // Determine which player and action based on key event
//        int playerId = determinePlayerId(e);
//        String direction = determineDirection(e);
//
//        if (!direction.isEmpty()) {
//            ModelPlayer player = modelgame.getPlayers().get(playerId);
//            player.move(direction);
//            updateGameView();
//        }


        // Update key state
        keyStates.put(e.getKeyCode(), isPressed);

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

        viewgame.updateGame();
    }

    private void updatePlayerMovement(int playerId, int keyCode, String direction) {
        if (keyStates.getOrDefault(keyCode, false)) {
            modelgame.getPlayers().get(playerId).move(direction);
        }
    }

    private String getLastDirection(int playerId) {
        ModelPlayer player = modelgame.getPlayers().get(playerId);
        return player.getDirection(); // Returns the last set direction of the player
    }

    private int determinePlayerId(KeyEvent e) {
        // Player 1 controls (WASD)
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A ||
                e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D) {
            return 0; // Index of Player 1
        }
        // Player 2 controls (Arrow keys)
        return 1; // Index of Player 2
    }

    private String determineDirection(KeyEvent e) {
        String direction = "";
        switch (e.getKeyCode()) {
            // Player 1 controls (WASD)
            case KeyEvent.VK_W: direction = "UP"; break;
            case KeyEvent.VK_A: direction = "LEFT"; break;
            case KeyEvent.VK_S: direction = "DOWN"; break;
            case KeyEvent.VK_D: direction = "RIGHT"; break;

            // Player 2 controls (Arrow keys)
            case KeyEvent.VK_UP: direction = "UP"; break;
            case KeyEvent.VK_LEFT: direction = "LEFT"; break;
            case KeyEvent.VK_DOWN: direction = "DOWN"; break;
            case KeyEvent.VK_RIGHT: direction = "RIGHT"; break;
        } return direction;
    }

    public void startGame() {
        modelgame.startGame();
        viewgame.initialiseGamePanel();
        viewgame.initialiseMenuBar();
        viewgame.updateGame();
        viewgame.getGamePanel().setVisible(true);
        viewgame.getGamePanel().requestFocusInWindow();

        //startGameLoop();
    }

    public ModelGame getGameModel() { return modelgame; }
    public ViewGame getGameView() { return viewgame;}
}
