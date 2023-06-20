package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAOProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("servleti/glasanje-like")
public class GlasanjeLajkajServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DAOProvider.getDao().addLike(Long.valueOf(req.getParameter("id")));
        resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + Long.valueOf(req.getParameter("pollID")));

    }
}
