package com.google.sps.storage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.util.SentimentAnalysisUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class CommentDatastore {
  private final ArrayList<String> comments;
  private final DatastoreService datastore;
  private Optional<SentimentAnalysisUtil> sentimentAnalysisUtil;

  public CommentDatastore() {
    comments = new ArrayList<>();
    datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      sentimentAnalysisUtil = Optional.ofNullable(new SentimentAnalysisUtil());
    } catch (IOException ioe) {
      sentimentAnalysisUtil = Optional.empty();
    }
  }

  public ArrayList<String> getComments() {
    comments.clear();

    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      String comment = (String) entity.getProperty("content");
      if (sentimentAnalysisUtil.isPresent()) {
        float sentimentScore = sentimentAnalysisUtil.get().getSentimentScore(comment);
        comment += String.format(" (Sentiment Score: %.2f)", sentimentScore);
      }
      comments.add(comment);
    }

    return comments;
  }

  public void initializeComments() {
    comments.add("Hello!");
    comments.add("Amazing website!");
    comments.add("Google SPS!");
  }

  public void add(String comment) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("content", comment);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
    datastore.put(commentEntity);
  }
}
