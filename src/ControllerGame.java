import java.awt.*;
import java.awt.event.KeyEvent;

public class ControllerGame {
    private ViewGame viewgame;
    private ModelGame modelgame;
    private ControllerStorage controllerstorage;

    public ControllerGame() {
        this.modelgame = new ModelGame();
        modelgame.startGame();
        this.controllerstorage = new ControllerStorage();
        this.viewgame = new ViewGame(this, controllerstorage);
    }

    public void initializePlayers(String name1, Color color1, String name2, Color color2) {
        modelgame.initializePlayers(name1, color1, name2, color2);
    }

    public void handlePlayerAction(KeyEvent e) {
        // Determine which player and action based on key event
        int playerId = determinePlayerId(e);
        String direction = determineDirection(e);

        if (!direction.isEmpty()) {
            ModelPlayer player = modelgame.getPlayers().get(playerId);
            player.move(direction);
            viewgame.updateGame(); // Update the game view after each move
            //modelgame.checkGameState(player);
//            modelgame.movePlayer(playerId, direction);
        }
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
        viewgame.updateGame();

        //TODO: extra i am putting here to see if it works, if it works keep it, if not, delete it
        viewgame.getGamePanel().setVisible(true);
        viewgame.getGamePanel().requestFocusInWindow();
    }

    public ModelGame getGameModel() { return modelgame; }

    public ViewGame getGameView() { return viewgame;}

    // Other controller methods...
}
