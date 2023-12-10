import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the controllers
        ControllerGame controllerGame = new ControllerGame();
        ControllerStorage controllerStorage = new ControllerStorage();

        // Initialize the Player Registration View
        ViewPlayer viewPlayer = new ViewPlayer(controllerGame);
    }
}