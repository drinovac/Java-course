package hr.fer.zemris.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@WebServlet("glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String fileNameRezultati = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String fileNameDefinicija = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

        if(!Files.exists(Paths.get(fileNameRezultati))) {
            Files.createFile(Paths.get(fileNameRezultati));
            req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
            return;
        }
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

        List<Result> winners = new LinkedList<>();
        winners.add(results.get(0));

        for(int i = 1; i < results.size(); i++) {
            if(results.get(i).getNumberOfVotes().equals(winners.get(0).getNumberOfVotes())) {
                winners.add(results.get(i));
            } else {
                break;
            }
        }


        req.setAttribute("results", results);
        req.setAttribute("winners", winners);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);

    }
}
