import java.awt.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * ViewGame is responsible for displaying the game to the user.
 * It is also responsible for displaying the high scores and the winner of the game.
 */
public class ViewGame {
    private JFrame frame;
    private Timer timer;
    private JPanel gamePanel;
    private JLabel timerLabel;
    private ModelGame modelgame;
    private JLabel currentLevelLabel;
    private ControllerGame controllergame;

    public ViewGame(ControllerGame controllergame) {
        this.controllergame = controllergame;
        this.modelgame = controllergame.getGameModel();
        this.timerLabel = new JLabel("Time: 0");
        this.currentLevelLabel = new JLabel("Level: 1");
        timer = new Timer(1000, e -> updateTimerLabel());
        timer.start();
    }


    /**
     * Initialises the game panel and the menu bar.
     */
    public void initialiseGamePanel() {
        if (frame != null) frame.getContentPane().removeAll();
        else frame = new JFrame("Tron Game");

        Dimension levelSize = controllergame.getCurrentLevelSize();
        frame.setSize(levelSize.width, levelSize.height);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) { super.paintComponent(g); drawGame(g); }
        };

        gamePanel.setPreferredSize(levelSize);
        gamePanel.setMinimumSize(levelSize);
        gamePanel.setMaximumSize(levelSize);

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.setBackground(Color.BLACK);
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controllergame.handlePlayerAction(e, true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                controllergame.handlePlayerAction(e, false);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Initialises the menu bar.
     */
    public void initialiseMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        //JMenuItem startMenuItem = new JMenuItem("Start Game");
        JMenuItem highScoresMenuItem = new JMenuItem("View High Scores");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        MenuListener menuListener = new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) { controllergame.pauseGame(true); }
            @Override
            public void menuDeselected(MenuEvent e) { controllergame.pauseGame(false); }
            @Override
            public void menuCanceled(MenuEvent e) { controllergame.pauseGame(false); }
        };

        gameMenu.addMenuListener(menuListener);
        highScoresMenuItem.addActionListener(e -> {
            controllergame.pauseGame(true);
            displayHighScores();
        });
        exitMenuItem.addActionListener(e -> System.exit(0));

        Component horizontalGap = Box.createHorizontalStrut(15);
        //gameMenu.add(startMenuItem);
        gameMenu.add(highScoresMenuItem);
        gameMenu.add(exitMenuItem);

        menuBar.add(gameMenu);
        menuBar.add(currentLevelLabel);
        menuBar.add(horizontalGap);
        menuBar.add(timerLabel);

        frame.setJMenuBar(menuBar);
    }

    /**
     * Updates the timer label.
     */
    public void updateTimerLabel() {
        int currentTime = controllergame.getGameModel().getTimer().getTime();
        timerLabel.setText("Time: " + currentTime);
    }

    /**
     * Updates the current level label.
     */
    public void updateCurrentLevelDisplay() {
        int currentLevel = controllergame.getGameModel().getCurrentLevelIndex() + 1;
        currentLevelLabel.setText("Level: " + currentLevel);
    }

    /**
     * Displays the high scores to the user.
     */
    private void displayHighScores() {
        //List<String> highScores = modelgame.modelstorage.getHighScore();
        //JOptionPane.showMessageDialog(frame, String.join("\n", highScores), "High Scores",JOptionPane.INFORMATION_MESSAGE);

        List<String> highScores = modelgame.modelstorage.getHighScore();
        JDialog highScoreDialog = new JDialog(frame, "High Scores", Dialog.ModalityType.APPLICATION_MODAL);
        JTextArea highScoreText = new JTextArea(String.join("\n", highScores));
        highScoreText.setEditable(false);

        highScoreDialog.add(new JScrollPane(highScoreText));
        highScoreDialog.setSize(300, 400);
        highScoreDialog.setLocationRelativeTo(frame);

        highScoreDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controllergame.pauseGame(false); // Resume the game when the dialog is closed
            }
        });

        highScoreDialog.setVisible(true);
    }

    /**
     * Draws the game to the screen.
     * @param g The graphics object to draw with.
     */
    private void drawGame(Graphics g) {
        // Draw both players from the start
        if (!modelgame.getPlayers().isEmpty()) { for (ModelPlayer player : modelgame.getPlayers()) drawLightTrail(g,
                player); }

        g.setColor(Color.WHITE);
        for(Point obstacle : modelgame.getCurrentLevel().getObstacles()) { g.fillRect(obstacle.x, obstacle.y, 12, 12); }
    }

    /**
     * Draws the light trail of a player.
     * @param g The graphics object to draw with.
     * @param player The player whose light trail to draw.
     */
    private void drawLightTrail(Graphics g, ModelPlayer player) {
        g.setColor(player.getColor());
        for (Point point : player.getLightTrail()) { g.fillRect(point.x, point.y, 5, 5); }
    }

    /**
     * Displays a dialog to the user when the game is over.
     * @param winnerName The name of the winner.
     */
    public void showWinnerDialog(String winnerName) {
        JOptionPane.showMessageDialog(frame, winnerName + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public JPanel getGamePanel() { return gamePanel; }

    /**
     * Repaints the game panel.
     */
    public void updateGame() { gamePanel.repaint(); }
}