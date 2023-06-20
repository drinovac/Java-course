package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.Result;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/servleti/glasanje-xls")
public class GlasanjeExcelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Result> results = DAOProvider.getDao().getResults(Long.valueOf(req.getParameter("pollID")));

        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet sheet = hwb.createSheet("Results");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Band ID");
        row.createCell(1).setCellValue("Band name");
        row.createCell(2).setCellValue("Song link");
        row.createCell(3).setCellValue("Num of votes");


        for(int i = 1; i <= results.size(); i++) {
            row = sheet.createRow(i);
            Result band = results.get(i - 1);
            row.createCell(0).setCellValue(band.getID());
            row.createCell(1).setCellValue(band.getName());
            row.createCell(2).setCellValue(band.getSongLink());
            row.createCell(3).setCellValue(band.getNumberOfVotes());
        }
        resp.setHeader("Content-Disposition", "attachment; filename=\"voting-results.xls\"");
        OutputStream outputStream = resp.getOutputStream();
        hwb.write(outputStream);
    }
}
