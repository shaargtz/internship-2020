package com.google.sps.servlets;

/** Object for a single comment. */
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
