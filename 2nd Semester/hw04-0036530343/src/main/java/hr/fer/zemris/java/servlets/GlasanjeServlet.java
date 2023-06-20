package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.Band;
import hr.fer.zemris.java.model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Band> bandList = DAOProvider.getDao().getBands(Long.valueOf(req.getParameter("pollID")));

        Poll poll = DAOProvider.getDao().getPoll(Long.valueOf(req.getParameter("pollID")));

        req.setAttribute("bands", bandList);
        req.setAttribute("poll", poll);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
