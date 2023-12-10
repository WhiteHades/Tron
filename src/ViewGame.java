import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

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

    public void initialiseMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem startMenuItem = new JMenuItem("Start Game");
        JMenuItem highScoresMenuItem = new JMenuItem("View High Scores");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        highScoresMenuItem.addActionListener(e -> displayHighScores());
        exitMenuItem.addActionListener(e -> System.exit(0));
        Component horizontalGap = Box.createHorizontalStrut(15);

        gameMenu.add(startMenuItem);
        gameMenu.add(highScoresMenuItem);
        gameMenu.add(exitMenuItem);

        menuBar.add(gameMenu);
        menuBar.add(currentLevelLabel);
        menuBar.add(horizontalGap);
        menuBar.add(timerLabel);

        frame.setJMenuBar(menuBar);
    }

    public void updateTimerLabel() {
        int currentTime = controllergame.getGameModel().getTimer().getTime();
        timerLabel.setText("Time: " + currentTime);
    }

    public void updateCurrentLevelDisplay() {
        int currentLevel = controllergame.getGameModel().getCurrentLevelIndex() + 1;
        currentLevelLabel.setText("Level: " + currentLevel);
    }

    private void displayHighScores() {
        List<String> highScores = modelgame.modelstorage.getHighScore();
        JOptionPane.showMessageDialog(frame, String.join("\n", highScores), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void drawGame(Graphics g) {
        // Draw both players from the start
        if (!modelgame.getPlayers().isEmpty()) { for (ModelPlayer player : modelgame.getPlayers()) drawLightTrail(g,
                player); }
    }

    private void drawLightTrail(Graphics g, ModelPlayer player) {
        g.setColor(player.getColor());
        for (Point point : player.getLightTrail()) { g.fillRect(point.x, point.y, 5, 5); }
    }

    public void showWinnerDialog(String winnerName) {
        JOptionPane.showMessageDialog(frame, winnerName + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public JPanel getGamePanel() { return gamePanel; }
    public void updateGame() { gamePanel.repaint(); }
}