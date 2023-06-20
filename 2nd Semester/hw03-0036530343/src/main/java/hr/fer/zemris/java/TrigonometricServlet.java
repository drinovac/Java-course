package hr.fer.zemris.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/trigonometric")
public class TrigonometricServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String a = req.getParameter("a");
        String b = req.getParameter("b");

        List<TrigonometricModel> list = new LinkedList<>();

        for (int i = Integer.valueOf(a); i <= Integer.valueOf(b); i++) {
            TrigonometricModel trig = new TrigonometricModel(i, Math.sin(i * Math.PI / 180), Math.cos(i * Math.PI / 180));
            list.add(trig);
        }

        req.getSession().setAttribute("values", list);

        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);

    }
}
