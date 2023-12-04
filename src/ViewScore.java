import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewScore {
    private JFrame frame;
    private JTextArea highScoreTextArea;
    private ControllerStorage controllerstorage;

    public ViewScore(ControllerStorage controllerstorage) {
        this.controllerstorage = controllerstorage;
        initializeScoreView();
    }

    private void initializeScoreView() {
        frame = new JFrame("High Scores");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(300, 200);

        highScoreTextArea = new JTextArea();
        highScoreTextArea.setEditable(false);
        frame.add(new JScrollPane(highScoreTextArea), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void display() {
        List<String> highScores = controllerstorage.getHighScores();
        StringBuilder sb = new StringBuilder();
        for (String score : highScores) {
            sb.append(score).append("\n");
        }
        highScoreTextArea.setText(sb.toString());
    }
}

