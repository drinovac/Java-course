package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * This class represents BarChart
 */
public class BarChart {

    /**
     * List of elements.
     */
    private List<XYValue> list;
    /**
     * Label on x-axis.
     */
    private String xLabel;
    /**
     * Label on y-axis.
     */
    private String yLabel;
    /**
     * Minimum value on y-axis.
     */
    private int miny;
    /**
     * Maximum value on y-axis.
     */
    private int maxy;
    /**
     * Difference between each value on y-axis.
     */
    private int deltay;

    /**
     * Constructor
     * @param list
     * @param xLabel
     * @param yLabel
     * @param miny
     * @param maxy
     * @param deltay
     */
    public BarChart(List<XYValue> list, String xLabel, String yLabel, int miny, int maxy, int deltay) {

        if(miny < 0 || maxy < miny) {
            throw new IllegalArgumentException();
        }
        for(XYValue value : list) {
            if(value.getY() < miny) {
                throw new IllegalArgumentException();
            }
        }
        this.list = list;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.miny = miny;
        this.maxy = maxy;
        this.deltay = (int) Math.ceil(deltay);
    }

    public List<XYValue> getList() {
        return list;
    }

    public String getxLabel() {
        return xLabel;
    }

    public String getyLabel() {
        return yLabel;
    }

    public int getMiny() {
        return miny;
    }

    public int getMaxy() {
        return maxy;
    }

    public int getDeltay() {
        return deltay;
    }
}
