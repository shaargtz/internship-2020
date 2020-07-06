/** Servlet that helps avoid getting the same video twice in a row.*/
@WebServlet("/video")
public class VideoServlet extends HttpServlet {

  public int lastVideoIndex = 4;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = "{";
    json += "\"lastVideoIndex\": ";
    json += "\"" + lastVideoIndex + "\"";
    json += "}";
    
    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
