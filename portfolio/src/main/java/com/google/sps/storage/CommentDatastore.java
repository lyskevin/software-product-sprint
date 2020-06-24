package com.google.sps.storage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.util.SentimentAnalysisUtil;
import java.util.ArrayList;

public class CommentDatastore {
  private final ArrayList<String> comments;
  private final DatastoreService datastore;
  private final SentimentAnalysisUtil sentimentAnalysisUtil;

  public CommentDatastore() {
    comments = new ArrayList<>();
    datastore = DatastoreServiceFactory.getDatastoreService();
    sentimentAnalysisUtil = new SentimentAnalysisUtil();
  }

  public ArrayList<String> getComments() {
    comments.clear();

    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      String comment = (String) entity.getProperty("content");
      float sentimentScore = sentimentAnalysisUtil.getSentimentScore(comment);
      comments.add(String.format("%s (Sentiment Score: %.2f)", comment, sentimentScore));
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
