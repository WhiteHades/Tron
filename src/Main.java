import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        ControllerGame controllerGame = new ControllerGame();
        ViewPlayer viewPlayer = new ViewPlayer(controllerGame);
    }
}