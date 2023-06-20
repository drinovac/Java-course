package hr.fer.zemris.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        List<Band> bandList = new LinkedList<>();

        String line;
        while((line = br.readLine()) != null) {
            String[] parts = line.split("\\t");

            Band band = new Band(parts[0], parts[1], parts[2]);

            bandList.add(band);
        }

        req.setAttribute("bands", bandList);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
