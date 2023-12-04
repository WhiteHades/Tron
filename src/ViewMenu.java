import javax.swing.*;

public class ViewMenu {
    private JFrame frame;
    private JMenuBar menuBar;
    private ControllerStorage controllerStorage;
    private JMenuItem highScoreItem, exitItem;

    public ViewMenu(ControllerStorage controllerStorage) {
        this.controllerStorage = controllerStorage;
        initializeMenu();
    }

    private void initializeMenu() {
        frame = new JFrame("Tron Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        menuBar = new JMenuBar();

        // High Score Menu Item
        highScoreItem = new JMenuItem("View High Scores");
        highScoreItem.addActionListener(e -> new ViewScore(controllerStorage).display());
        menuBar.add(highScoreItem);

        // Exit Menu Item
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menuBar.add(exitItem);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    // Getter for the frame, in case it needs to be accessed from outside
    public JFrame getFrame() {
        return frame;
    }
}
