public class ModelLevel {
    private int level;
    private int width;
    private int height;

    public ModelLevel(int number, int width, int height) {
        this.level = number;
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getLevel() { return level; }
}