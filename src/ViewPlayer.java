import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * View for player registration
 */
public class ViewPlayer {
    private JFrame frame;
    private JTextField playerNameField1, playerNameField2;
    private JComboBox<String> colorComboBox1, colorComboBox2;
    private ControllerGame controllergame;

    public ViewPlayer(ControllerGame controllergame) {
        this.controllergame = controllergame;
        initializePlayerView();
    }

    /**
     * Initialize the player registration view
     */
    private void initializePlayerView() {
        frame = new JFrame("Player Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));

        // Maps for color names to Color objects
        Map<String, Color> colorMap = new HashMap<>();
        colorMap.put("Cyan", Color.CYAN);
        colorMap.put("Green", Color.GREEN);
        colorMap.put("Red", Color.RED);
        colorMap.put("Yellow", Color.YELLOW);
        colorMap.put("Purple", Color.MAGENTA);

        // Player 1
        frame.add(new JLabel("Player 1 Name:"));
        playerNameField1 = new JTextField();
        frame.add(playerNameField1);

        frame.add(new JLabel("Player 1 Color:"));
        colorComboBox1 = new JComboBox<>(new String[]{"Cyan", "Green", "Red", "Yellow", "Purple"});
        frame.add(colorComboBox1);

        // Player 2
        frame.add(new JLabel("Player 2 Name:"));
        playerNameField2 = new JTextField();
        frame.add(playerNameField2);

        frame.add(new JLabel("Player 2 Color:"));
        colorComboBox2 = new JComboBox<>(new String[]{"Cyan", "Green", "Red", "Yellow", "Purple"});
        frame.add(colorComboBox2);

        // Register button
        JButton registerButton = new JButton("Register Players");
        frame.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name1 = playerNameField1.getText();
                Color color1 = colorMap.get(colorComboBox1.getSelectedItem());
                String name2 = playerNameField2.getText();
                Color color2 = colorMap.get(colorComboBox2.getSelectedItem());
                controllergame.initializePlayers(name1, color1, name2, color2);
                controllergame.startGame();
                frame.dispose();
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
