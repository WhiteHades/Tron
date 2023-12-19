import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

/**
 * ModelLevel.java
 */
public class ModelLevel {
    private int level;
    private int width;
    private int height;
    private List<Point> obstacles;

    public ModelLevel(int number, int width, int height) {
        this.level = number;
        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();
    }

    public void setObstacles(List<Point> obstacles) { this.obstacles = obstacles; }
    public List<Point> getObstacles() { return obstacles; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}