import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        ControllerGame controllerGame = new ControllerGame();
        ControllerStorage controllerStorage = new ControllerStorage();
        ViewPlayer viewPlayer = new ViewPlayer(controllerGame);
    }
}