package hr.fer.zemris.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        if (id != null) {
            String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

            if(!Files.exists(Paths.get(fileName))) {
                Files.createFile(Paths.get(fileName));
            }

            List<String> lines = Files.readAllLines(Paths.get(fileName));

            Map<String, String> results = new HashMap<>();

            for(String line: lines) {
                String parts[] = line.split("\t");
                if(parts[0].equals(id)) {
                    int res = Integer.parseInt(parts[1]);
                    res++;
                    parts[1] = String.valueOf(res);
                }
                results.put(parts[0], parts[1]);
            }

            OutputStream outputStream = Files.newOutputStream(Paths.get(fileName));
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry: results.entrySet()) {
                sb.append(entry.getKey()).append("\t");
                sb.append(entry.getValue()).append("\n");
            }
            outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));

        }
        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }
}
