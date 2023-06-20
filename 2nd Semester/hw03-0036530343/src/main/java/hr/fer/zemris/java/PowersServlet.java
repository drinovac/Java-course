package hr.fer.zemris.java;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/powers")
public class PowersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));
        int n = Integer.parseInt(req.getParameter("n"));

        if(!(a >= -100 && a <= 100 && b >=-100 && b <= 100 && n >= 1 && n <= 5)) {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }

        HSSFWorkbook hwb = new HSSFWorkbook();

        for(int i = 1; i <= n; i++) {
            HSSFSheet sheet = hwb.createSheet("Sheet pow " + i);
            for(int j = a; j <= b; j++) {
                HSSFRow row = sheet.createRow(j - a);
                row.createCell(0).setCellValue(j);
                row.createCell(1).setCellValue(Math.pow(j, i));
            }
        }

        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
        OutputStream outputStream = resp.getOutputStream();
        hwb.write(outputStream);
    }
}
