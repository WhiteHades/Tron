import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModelPlayer {
    private String name;
    private Color color;
    private int score;
    private int xPosition;
    private int yPosition;
    private String direction;
    private List<Point> lightTrail;
    int speed = 5;

    public ModelPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
        this.score = 0;
        this.xPosition = 0;
        this.yPosition = 0;
        this.direction = "UP";
        this.lightTrail = new ArrayList<>();
    }

    public void move(String newDirection) {
        if(!newDirection.isEmpty() && !this.direction.equals(newDirection)) { this.direction = newDirection; }
        //this.direction = newDirection;

        // Update position based on the direction
        switch (direction) {
            case "UP": yPosition -= speed; break;
            case "DOWN": yPosition += speed; break;
            case "LEFT": xPosition -= speed; break;
            case "RIGHT": xPosition += speed; break;
            default: break;
        } updateLightTrail();
    }

    public void resetPlayer(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        this.direction = "UP";
        this.lightTrail = new ArrayList<>();
        updateLightTrail();
    }

    public void setDirection(String direction) { this.direction = direction; }
    private void updateLightTrail() { lightTrail.add(new Point(xPosition, yPosition)); }
    public void resetLightTrail() { this.lightTrail.clear(); }
    public void setXPosition(int xPosition) { this.xPosition = xPosition; }
    public void setYPosition(int yPosition) { this.yPosition = yPosition; }
    public List<Point> getLightTrail() { return lightTrail; }
    public String getName() { return name; }
    public Color getColor() { return color; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getXPosition() { return xPosition; }
    public int getYPosition() { return yPosition; }
    public String getDirection() { return direction; }
}