package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random; 
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that helps avoid getting the same video twice in a row.*/
@WebServlet("/video")
public class VideoServlet extends HttpServlet {

  /**
   * Initialized as -1 since it is a non-existing index, so all videos can be
   * selected on the first GET request
   */
  public int lastVideoIndex = -1;

  ArrayList<String> videos = new ArrayList<String>(
      Arrays.asList(
        "https://www.youtube.com/watch?v=Ng_Im-qsWzc&list=LLGpjNAdp_solk9m7e4SaPsg&index=8",
        "https://www.youtube.com/watch?v=sYd_-pAfbBw&list=LLGpjNAdp_solk9m7e4SaPsg&index=48",
        "https://www.youtube.com/watch?v=xuCn8ux2gbs&list=LLGpjNAdp_solk9m7e4SaPsg&index=61",
        "https://www.youtube.com/watch?v=huEtJw7pfLk&list=LLGpjNAdp_solk9m7e4SaPsg&index=68",
        "https://www.youtube.com/watch?v=Uu5zGHjRaMo&list=LLGpjNAdp_solk9m7e4SaPsg&index=22"
  ));

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Random randomGenerator = new Random();

    // Generate a random index from the videos array.
    int nextVideoIndex = randomGenerator.nextInt(videos.size());

    // Check that the last index is not the same as the next.
    if (nextVideoIndex == lastVideoIndex) {
      nextVideoIndex++;
      nextVideoIndex = nextVideoIndex % videos.size();
    }

    String json = "{";
    json += "\"videoURL\": ";
    json += "\"" + videos.get(nextVideoIndex) + "\"";
    json += "}";

    lastVideoIndex = nextVideoIndex;
    
    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
