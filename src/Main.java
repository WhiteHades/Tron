import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // Creating and showing the application's GUI.
        EventQueue.invokeLater(() -> {
            // Initialize the controllers
            ControllerGame controllerGame = new ControllerGame();
            ControllerStorage controllerStorage = new ControllerStorage();
            ControllerInformation controllerInformation = new ControllerInformation(controllerGame.getGameModel());
            //test

            // Initialize the Player Registration View
            ViewPlayer viewPlayer = new ViewPlayer(controllerInformation, controllerGame);

            // The actual game flow will be managed by the views and controllers.
            // The player registration will trigger the game start in the ViewPlayer class.
        });
    }
}