package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * This is BarChart components that draws chart.
 */
public class BarChartComponent extends JComponent {

    /**
     * BarChart reference.
     */
    private BarChart chart;

    /**
     * Constructor.
     * @param chart
     */
    public BarChartComponent(BarChart chart) {
        this.chart = chart;
    }

    /**
     * This method draws chart component.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        /**
         * Coordinate system (0,0)
         */
        int x0 = 70 + String.valueOf(chart.getMaxy()).length() * 5;
        int y0 = getHeight() - 70;
        /**
         * x-axis and y-axis
         */
        g2d.drawLine(x0, y0, x0, 30);
        g2d.drawLine(x0, y0, getWidth() - 30, y0);

        /**
         * arrows
         */
        g2d.drawPolygon(new int[]{x0, x0 - 5, x0 + 5}, new int[]{20,30,30},3);
        g2d.drawPolygon(new int[]{getWidth() - 20, getWidth() - 30, getWidth() - 30},
                new int[]{y0, y0-5, y0+5},3);

        /**
         * labels
         */
        AffineTransform defaultAt = g2d.getTransform();

        AffineTransform at = new AffineTransform(defaultAt);
        at.rotate(- Math.PI / 2);
        g2d.setTransform(at);
        g2d.drawString(chart.getyLabel(), -(getHeight() + 50) / 2, 50);


        g2d.setTransform(defaultAt);

        g2d.drawString(chart.getxLabel(), (getWidth() - 50) / 2, y0 + 40);

        /**
         * values on y-axis
         */
        int numOfLines = (chart.getMaxy() - chart.getMiny()) / chart.getDeltay();
        int miny = chart.getMiny() - chart.getDeltay();

        for(int i = 0; i <= numOfLines; i++) {
            int y = y0 - i * (y0 - 40) / numOfLines;
            g2d.drawLine(x0, y, x0-5, y);
            g2d.drawString(String.valueOf(miny += chart.getDeltay()),
                    55 + (String.valueOf(chart.getMaxy()).length() - String.valueOf(miny).length()) * 5, y + 4);
        }
        /**
         * values on x-axis
         */
        int numOfColumns = chart.getList().size();
        int columnWidth = (getWidth() - x0 - 40) / numOfColumns;

        List<XYValue> xyValues = chart.getList();

        for(int i = 0; i <= numOfColumns; i++) {
            int x = x0 + i * columnWidth;
            g2d.drawLine(x, y0, x, y0+5);
            if(i < numOfColumns) {
                XYValue value = xyValues.get(i);
                g2d.drawString(String.valueOf(value.getX()), x + columnWidth / 2, y0 + 20);
                g2d.setColor(new Color(250,120,80));
                Polygon p = new Polygon(new int[]{x + 2, x + columnWidth - 2, x + columnWidth - 2, x + 2},
                        new int[]{y0, y0, y0 - value.getY() * (y0 - 40) / numOfLines  / chart.getDeltay(), y0 - value.getY()  * (y0 - 40) / numOfLines / chart.getDeltay()}, 4);
                g2d.drawPolygon(p);
                g2d.fill(p);
                g2d.setColor(Color.BLACK);
            }
        }
    }
}
