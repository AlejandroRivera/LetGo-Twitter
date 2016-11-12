package com.letgo.twitter.core.internal.models;

import com.letgo.twitter.core.api.models.Tweet;

import java.util.Date;

public class TweetPojo implements Tweet {

  public String message;

  public Date createdAt;

  public TweetPojo() {
  }

  public TweetPojo(String message, Date createdAt) {
    this.message = message;
    this.createdAt = new Date(createdAt.getTime());
  }

  @Override
  public String getMessage() {
    return message;
  }

  public Tweet setMessage(String message) {
    this.message = message;
    return this;
  }

  @Override
  public Date getCreatedAt() {
    return new Date(createdAt.getTime());
  }

  public Tweet setCreatedAt(Date createdAt) {
    this.createdAt = new Date(createdAt.getTime());
    return this;
  }
}
