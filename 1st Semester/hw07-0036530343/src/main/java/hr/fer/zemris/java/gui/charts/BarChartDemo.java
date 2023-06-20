package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BarChartDemo extends JFrame {

    public BarChartDemo(String file, BarChart barChart) {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        BarChartComponent component = new BarChartComponent(barChart);
        setSize(600,500);
        getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel(file);
        label.setSize(label.getMinimumSize());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label, BorderLayout.NORTH);
        getContentPane().add(component, BorderLayout.CENTER);
    }


    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //String file = br.readLine();
        String file = "chartDescription.txt";

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(file))))) {
            String xLabel = reader.readLine();
            String yLabel = reader.readLine();
            String[] inputs = reader.readLine().split(" ");
            int miny = Integer.parseInt(reader.readLine());
            int maxy = Integer.parseInt(reader.readLine());
            int deltay = Integer.parseInt(reader.readLine());

            List<XYValue> list = new ArrayList<>();
            for(String input: inputs) {
                list.add(new XYValue(Integer.parseInt(input.split(",")[0]), Integer.parseInt(input.split(",")[1])));
            }

            BarChart barChart = new BarChart(list, xLabel, yLabel, miny, maxy, deltay);

            SwingUtilities.invokeLater(() -> {
                new BarChartDemo(file, barChart).setVisible(true);
            });

        }  catch (IOException exc) {
            System.out.println(exc);
        }
    }
}
