package com.google.sps.storage;

import java.util.ArrayList;

public class CommentDatastore {

  private ArrayList<String> comments;

  public CommentDatastore() {
    comments = new ArrayList<>();
  }

  public void initializeComments() {
    comments.add("Hello!");
    comments.add("Amazing website!");
    comments.add("Google SPS!");
  }

  public ArrayList<String> getComments() {
    return comments;
  }
}
