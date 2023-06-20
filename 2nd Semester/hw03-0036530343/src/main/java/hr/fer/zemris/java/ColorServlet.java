package hr.fer.zemris.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/setcolor")
public class ColorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String color = req.getParameter("color");

        if(color != null) {
            req.getSession().setAttribute("pickedBgCol", color);
        }

        req.getRequestDispatcher("colors.jsp").forward(req, resp);
    }
}
