package com.letgo.twitter.web.dto;

import com.letgo.twitter.core.api.models.Tweet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TweetDto {

  public String message;

  @JsonProperty("created_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  public Date createdAt;

  public TweetDto() {
  }

  public TweetDto(String message, Date createdAt) {
    this.message = message;
    this.createdAt = new Date(createdAt.getTime());
  }

  public static TweetDto fromTweet(Tweet tweet) {
    return new TweetDto(tweet.getMessage(), tweet.getCreatedAt());
  }

  public String getMessage() {
    return message;
  }

  public TweetDto setMessage(String message) {
    this.message = message;
    return this;
  }

  public Date getCreatedAt() {
    return new Date(createdAt.getTime());
  }

  public TweetDto setCreatedAt(Date createdAt) {
    this.createdAt = new Date(createdAt.getTime());
    return this;
  }
}
