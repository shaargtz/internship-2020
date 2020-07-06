package com.google.sps.servlets;

import java.util.ArrayList;

/** List of comments to be displayed. */
public final class TestCommentObject {

  private final ArrayList<String> commentList;

  public TestCommentObject(ArrayList<String> commentList) {
    this.commentList = commentList;
  }
}
