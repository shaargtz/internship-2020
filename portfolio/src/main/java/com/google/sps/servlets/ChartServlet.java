package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that gets data from a csv and parses it as to use it in a chart.*/
@WebServlet("/chart-data")
public class VideoServlet extends HttpServlet {
  private LinkedHashMap<Integer, Integer> memeSearchTrend = new LinkedHashMap<>();

  /** Parses csv data a single time instead of on every request.*/
  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/meme-trend.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      String date = cells[0];
      Integer interest = Integer.valueOf(cells[1]);

      memeSearchTrend.put(date, interest);
    }
    scanner.close();
  }

  /** Sends parsed data in JSON format.*/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(memeSearchTrend);
    response.getWriter().println(json);
  }
}
