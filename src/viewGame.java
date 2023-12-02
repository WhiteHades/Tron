import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class viewGame {
    private controllerGame controllergame;
    private JPanel gamePanel;

    public viewGame(controllerGame controllergame) {
        this.controllergame = controllergame;
        initialiseGamePanel();
    }

    private void initialiseGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controllergame.handlePlayerAction(e);
            }
        });
    }

    public void handleKeyPress(KeyEvent e) {
        // Assuming player 1 is at index 0 and player 2 is at index 1 in the players list
        controllergame.handlePlayerAction(e);
    }

    public JPanel getGamePanel() { return gamePanel; }
}
