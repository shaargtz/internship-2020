package com.google.sps.servlets;

import com.google.sps.data.ServerStats;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Date;
import java.util.Random; 
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that helps avoid getting the same video twice in a row.*/
@WebServlet("/video")
public class VideoServlet extends HttpServlet {

  public int lastVideoIndex = 4;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    int nextVideoIndex = 

    String json = "{";
    json += "\"nextVideoIndex\": ";
    json += "\"" + nextVideoIndex + "\"";
    json += "}";


    
    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
