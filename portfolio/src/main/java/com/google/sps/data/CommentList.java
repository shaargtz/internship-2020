package com.google.sps.servlets;

import java.util.ArrayList;

/** List of comments to be displayed. */
public final class CommentList {

  private final ArrayList<String> comments = new ArrayList<String>();

  public void addComment(String comment) {
    comments.add(comment);
  }
}
