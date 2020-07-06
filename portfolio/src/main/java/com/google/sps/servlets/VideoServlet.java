package com.google.sps.servlets;

import java.io.IOException;
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

    Random randomGenerator = new Random();

    // Generate a random index from 0 to 5.
    int nextVideoIndex = randomGenerator.nextInt(5);

    // Check that the last index is not the same as the next.
    if (nextVideoIndex == lastVideoIndex) {
      nextVideoIndex++;
      nextVideoIndex = nextVideoIndex % 5;
    }

    String json = "{";
    json += "\"nextVideoIndex\": ";
    json += "\"" + nextVideoIndex + "\"";
    json += "}";

    lastVideoIndex = nextVideoIndex;
    
    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
