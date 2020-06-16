package com.google.sps.storage;

import java.util.ArrayList;

public class CommentDatastore {

  private ArrayList<String> comments;

  public CommentDatastore() {
    comments = new ArrayList<>();
  }

  public ArrayList<String> getComments() {
    return comments;
  }

  public void initializeComments() {
    comments.add("Hello!");
    comments.add("Amazing website!");
    comments.add("Google SPS!");
  }

  public void add(String comment) {
    comments.add(comment);
  }

}
