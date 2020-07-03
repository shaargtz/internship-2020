package com.google.sps.servlets;

import java.util.ArrayList;

public final class TestCommentObject {

  private final ArrayList<String> commentList = new ArrayList<String>();

  public void addComment(String comment) {
    commentList.add(comment);
  }
}
