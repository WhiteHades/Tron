import java.util.*;

public class ControllerStorage {
    private ModelStorage modelstorage;

    public ControllerStorage() {
        this.modelstorage = new ModelStorage();
    }

    public void updateScore (String name, int score) {
        modelstorage.updateScore(name, score);
    }

    public List<String> getHighScores() {
        return modelstorage.getHighScore();
    }
}
