package com.google.sps.servlets;

import java.util.ArrayList;

public final class CommentList {

  private final ArrayList<String> comments = new ArrayList<String>();

  public void addComment(String comment) {
    comments.add(comment);
  }
}
