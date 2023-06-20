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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@WebServlet("servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Long pollID = Long.valueOf(req.getParameter("pollID"));

        List<Result> results = DAOProvider.getDao().getResults(pollID);
        List<Result> winners = DAOProvider.getDao().getWinners(pollID);

        req.setAttribute("results", results);
        req.setAttribute("winners", winners);
        req.setAttribute("pollID", pollID);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);

    }
}
