package com.google.sps.servlets;

/** Object for a single comment. Used for JSON conversion in the servlet. */
public final class Comment {

  private final String text;
  private final String author;
  private final long timestamp;

  public Comment(String text, String author, long timestamp) {
    this.text = text;
    this.author = author;
    this.timestamp = timestamp;
  }
}
