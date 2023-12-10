import java.awt.*;
import java.io.File;
import javax.swing.*;
import java.util.List;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;

public class ViewGame {
    private JFrame frame;
    private Timer timer;
    private JPanel gamePanel;
    private JLabel timerLabel;
    private ModelGame modelgame;
    private ControllerGame controllergame;
    private ControllerStorage controllerstorage;
    private BufferedImage player1Image, player2Image;

    public ViewGame(ControllerGame controllergame, ControllerStorage controllerstorage) {
        this.controllergame = controllergame;
        this.controllerstorage = controllerstorage;
        this.modelgame = controllergame.getGameModel();
        this.timerLabel = new JLabel("Time: 0");
        timer = new Timer(1000, e -> updateTimerLabel());
        timer.start();
        //loadImages();
        //initialiseGamePanel();
        //initialiseMenuBar();
    }

//    private void loadImages() {
//        try {
//            if (modelgame.getPlayers().size() >= 2) {
//                player1Image = loadImageForColor(modelgame.getPlayers().get(0).getColor());
//                player2Image = loadImageForColor(modelgame.getPlayers().get(1).getColor());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle the exception if images cannot be loaded
//        }
//    }

    private BufferedImage loadImageForColor(Color color) throws IOException {
        String colorName = getColorName(color);
        return ImageIO.read(new File("assets/player" + colorName + ".png"));
    }

    private String getColorName(Color color) {
        if (Color.CYAN.equals(color)) return "CYAN";
        if (Color.GREEN.equals(color)) return "GREEN";
        if (Color.RED.equals(color)) return "RED";
        if (Color.YELLOW.equals(color)) return "YELLOW";
        if (Color.MAGENTA.equals(color)) return "PURPLE"; // Assuming purple is represented by MAGENTA
        return "DEFAULT"; // Default case
    }

    public void initialiseGamePanel() {
        if (frame != null) {
            frame.getContentPane().removeAll();
//            frame.repaint();
        } else {
            frame = new JFrame("Tron Game");
        }

        Dimension levelSize = controllergame.getCurrentLevelSize();
        frame.setSize(levelSize.width, levelSize.height);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); drawGame(g);
            }
        };

        gamePanel.setPreferredSize(levelSize);
        gamePanel.setMinimumSize(levelSize);
        gamePanel.setMaximumSize(levelSize);

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.setBackground(Color.BLACK); // Set the background to black
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
        frame.revalidate(); // Refresh layout
        frame.repaint();    // Refresh visual appearance
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void initialiseMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem startMenuItem = new JMenuItem("Start Game");
        JMenuItem highScoresMenuItem = new JMenuItem("View High Scores");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        //startMenuItem.addActionListener(e -> controllergame.startGame());
        highScoresMenuItem.addActionListener(e -> displayHighScores());
        exitMenuItem.addActionListener(e -> System.exit(0));

        gameMenu.add(startMenuItem);
        gameMenu.add(highScoresMenuItem);
        gameMenu.add(exitMenuItem);

        menuBar.add(gameMenu);
        menuBar.add(timerLabel);

        frame.setJMenuBar(menuBar);

    }

    public void updateTimerLabel() {
        int currentTime = controllergame.getGameModel().getTimer().getTime();
        timerLabel.setText("Time: " + currentTime);
    }

    public void updateTimerDisplay(int currentTime) {
        timerLabel.setText("Time: " + currentTime);
    }

    private void displayHighScores() {
        List<String> highScores = controllerstorage.getHighScores();
        JOptionPane.showMessageDialog(frame, String.join("\n", highScores), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void drawGame(Graphics g) {
        // Draw both players from the start
        if (!modelgame.getPlayers().isEmpty()) {
            drawPlayer(g, modelgame.getPlayers().get(0), player1Image);
            drawPlayer(g, modelgame.getPlayers().get(1), player2Image);

            // Draw light trails for both players
            for (ModelPlayer player : modelgame.getPlayers()) drawLightTrail(g, player);
        }
    }

    private void drawPlayer(Graphics g, ModelPlayer player, BufferedImage image) {
        if (image != null) {
            g.drawImage(image, player.getXPosition(), player.getYPosition(), null);
        }
    }

    private void drawLightTrail(Graphics g, ModelPlayer player) {
        g.setColor(player.getColor()); // Set the light trail color
        for (Point point : player.getLightTrail()) {
            g.fillRect(point.x, point.y, 5, 5); // Draw each segment of the light trail
        }
    }

    public void showWinnerDialog(String winnerName) {
        JOptionPane.showMessageDialog(frame, winnerName + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public JPanel getGamePanel() { return gamePanel; }
    public void updateGame() { gamePanel.repaint(); }
}