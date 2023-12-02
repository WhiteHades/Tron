import java.awt.event.KeyEvent;

public class controllerGame {
    private modelGame modelgame;

    public controllerGame() {
        this.modelgame = new modelGame();
        //initialise game..
    }

    public void handlePlayerAction(KeyEvent e) {
        // Determine which player and action based on key event
        // and update the model accordingly

        // Assuming player 1 is at index 0 and player 2 is at index 1 in the players list
        int playerId = (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A ||
                e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D) ? 0 : 1;

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
        }

        if (!direction.isEmpty()) { modelgame.movePlayer(playerId, direction); }
    }

    // Other controller methods...
}
