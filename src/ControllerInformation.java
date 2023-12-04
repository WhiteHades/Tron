import java.awt.*;

public class ControllerInformation {
    private ModelGame modelgame;

    public ControllerInformation(ModelGame modelgame) {
        this.modelgame = modelgame;
    }

    public void registerPlayer(String name1, Color color1, String name2, Color color2) {
        modelgame.initializePlayers(name1, color1, name2, color2);
    }

    // Additional methods to fetch or update player information if needed
}
