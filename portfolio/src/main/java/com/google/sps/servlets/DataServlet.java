// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.servlets.Comment;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles uploading user comments to Datastore and querying
 * existing comments to send them as JSON to the client.
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  /**
  * Queries Datastore and sends existing comments to the client in 
  * JSON string format.
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    // Datastore is being queried for user comments.
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Comment> comments = new ArrayList<Comment>();
    for (Entity entity : results.asIterable()) {
      String text = (String) entity.getProperty("text");
      String author = (String) entity.getProperty("author");
      long timestamp = (long) entity.getProperty("timestamp");

      Comment comment = new Comment(text, author, timestamp);
      comments.add(comment);
    }

    Gson gson = new Gson();
    String json = gson.toJson(comments);

    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
  * Receives user comments from the client and uploads them to Datastore as
  * a 'Comment' entity.
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentString = request.getParameter("comment-input");
    long timestamp = System.currentTimeMillis();

    // New entity for Datastore is created.
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", commentString);
    commentEntity.setProperty("author", "anonymous");
    commentEntity.setProperty("timestamp", timestamp);

    // Datastore is used to store comments remotely.
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    
    response.sendRedirect("/index.html");
  }
}
