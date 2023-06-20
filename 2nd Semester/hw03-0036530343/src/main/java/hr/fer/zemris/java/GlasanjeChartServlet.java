package hr.fer.zemris.java;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/glasanje-chart")
public class GlasanjeChartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");

        String fileNameRezultati = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String fileNameDefinicija = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

        Map<String, String> rezultati = new HashMap<>();
        List<String> rezLines = Files.readAllLines(Paths.get(fileNameRezultati));

        for (String line: rezLines) {
            rezultati.put(line.split("\\t")[0], line.split("\\t")[1]);
        }

        List<String> lines = Files.readAllLines(Paths.get(fileNameDefinicija));

        List<Result> results = new ArrayList<>();
        for (String line: lines) {
            String[] parts = line.split("\\t");

            results.add(new Result(parts[0], parts[1], parts[2], rezultati.get(parts[0])));
        }
        results.sort((o1, o2) -> o2.getNumberOfVotes().compareTo(o1.getNumberOfVotes()));

        PieDataset dataset = createDataset(results);

        JFreeChart chart = createChart(dataset, "Voting results");

        resp.getOutputStream().write(ChartUtils.encodeAsPNG(chart.createBufferedImage(500,350)));
    }
    private PieDataset createDataset(List<Result> results) {
        DefaultPieDataset result = new DefaultPieDataset();

        for(Result res: results) {
            result.setValue(res.getName(), Double.parseDouble(res.getNumberOfVotes()));
        }

        return result;
    }
    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart3D(
                title,                  // chart title
                dataset,                // data
                true,                   // include legend
                true,
                false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
}
