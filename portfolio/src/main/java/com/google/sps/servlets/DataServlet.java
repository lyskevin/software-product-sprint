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

<<<<<<< HEAD
<<<<<<< HEAD
import com.google.sps.storage.CommentDatastore;
import com.google.sps.util.JsonUtil;

=======
=======
>>>>>>> Add datastore for comments
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
>>>>>>> Add datastore for comments
import java.io.IOException;
import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static CommentDatastore commentDatastore;
  private String json;

  @Override
  public void init() {
    commentDatastore = new CommentDatastore();
    commentDatastore.initializeComments();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    json = JsonUtil.convertToJson(commentDatastore.getComments());
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  private void queryCommentsFromDatabase() {
    comments.clear();

    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      comments.add((String) entity.getProperty("content"));
    }
  }

  private void queryCommentsFromDatabase() {
    comments.clear();

    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      comments.add((String) entity.getProperty("content"));
    }
  }

  private String convertCommentsToJson() {
    Gson gson = new Gson();
    return gson.toJson(comments);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    addCommentToDatastore(request);
    response.sendRedirect("./index.html");
  }

  private void addCommentToDatastore(HttpServletRequest request) {
    Optional<String> comment = getComment(request);

    if (comment.isPresent()) {
      commentDatastore.add(comment.get());
    }

    response.sendRedirect("./index.html");
  }

  private Optional<String> getComment(HttpServletRequest request) {
    String comment = request.getParameter("comment-input-field");
    if (comment == null) {
      return Optional.empty();
    }
    String commentContent = comment.trim();
    if (commentContent.equals("")) {
      return Optional.empty();
    }
    return Optional.ofNullable(commentContent);
  }
}
