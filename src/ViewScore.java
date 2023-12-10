import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewScore {
    private JFrame frame;
    private JTextArea highScoreTextArea;
    public ControllerGame controllergame;

    public ViewScore() { initializeScoreView(); }

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
        List<String> highScores = controllergame.modelgame.modelstorage.getHighScore();
        StringBuilder stringbuilder = new StringBuilder();
        for (String score : highScores) stringbuilder.append(score).append("\n");
        highScoreTextArea.setText(stringbuilder.toString());
    }
}

