import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the controllers
        ControllerGame controllerGame = new ControllerGame();
        ControllerStorage controllerStorage = new ControllerStorage();
        ControllerInformation controllerInformation = new ControllerInformation(controllerGame.getGameModel());

        // Initialize the Player Registration View
        ViewPlayer viewPlayer = new ViewPlayer(controllerInformation, controllerGame);
    }
}