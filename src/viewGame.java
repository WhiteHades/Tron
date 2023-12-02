import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class viewGame {
    private controllerGame controllergame;
    private modelGame modelgame;
    private JPanel gamePanel;
    private BufferedImage player1Image, player2Image;

    public viewGame(controllerGame controllergame) {
        this.controllergame = controllergame;
        this.modelgame = controllergame.getGameModel();
        loadImages();
        initialiseGamePanel();
    }

    private void loadImages() {
        try {
            if (modelgame.getPlayers().size() >= 2) {
                player1Image = loadImageForColor(modelgame.getPlayers().get(0).getColor());
                player2Image = loadImageForColor(modelgame.getPlayers().get(1).getColor());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception if images cannot be loaded
        }
    }

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

    private void initialiseGamePanel() {
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); drawGame(g);
            }
        };

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.setBackground(Color.BLACK); // Set the background to black
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controllergame.handlePlayerAction(e);
            }
        });

        gamePanel.setBackground(Color.BLACK);
    }

    private void drawGame(Graphics g) {
        // Draw players and light trails
        if (modelgame.getPlayers().size() >= 2) {
            drawPlayer(g, modelgame.getPlayers().get(0), player1Image);
            drawPlayer(g, modelgame.getPlayers().get(1), player2Image);
            for (modelPlayer player : modelgame.getPlayers()) {
                drawLightTrail(g, player);
            }
        }
    }

    private void drawPlayer(Graphics g, modelPlayer player, BufferedImage image) {
        if (image != null) {
            g.drawImage(image, player.getXPosition(), player.getYPosition(), null);
        }
    }

    private void drawLightTrail(Graphics g, modelPlayer player) {
        g.setColor(player.getColor()); // Set the light trail color
        for (Point point : player.getLightTrail()) {
            g.fillRect(point.x, point.y, 5, 5); // Draw each segment of the light trail
        }
    }

    public JPanel getGamePanel() { return gamePanel; }
    public void updateGame() { gamePanel.repaint(); }
}