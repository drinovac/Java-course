package hr.fer.zemris.java.gui.charts;

/**
 * This class represents pair to ints.
 */
public class XYValue {
    /**
     * X value.
     */
    private int x;
    /**
     * Y value
     */
    private int y;

    /**
     * Constructor.
     * @param x
     * @param y
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
