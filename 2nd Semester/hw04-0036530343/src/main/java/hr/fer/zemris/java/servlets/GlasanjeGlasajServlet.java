package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DAOProvider.getDao().addVote(Long.valueOf(req.getParameter("id")));
        resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + Long.valueOf(req.getParameter("pollID")));
    }
}
