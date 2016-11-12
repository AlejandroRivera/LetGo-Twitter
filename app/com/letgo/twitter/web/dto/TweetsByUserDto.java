package com.letgo.twitter.web.dto;

import com.letgo.twitter.core.api.models.Tweet;

import java.util.List;

public class TweetsByUserDto {
  private String username;
  private List<Tweet> tweets;

  public TweetsByUserDto(String username, List<Tweet> tweets) {
    this.username = username;
    this.tweets = tweets;
  }

  public String getUsername() {
    return username;
  }

  public TweetsByUserDto setUsername(String username) {
    this.username = username;
    return this;
  }

  public List<Tweet> getTweets() {
    return tweets;
  }

  public TweetsByUserDto setTweets(List<Tweet> tweets) {
    this.tweets = tweets;
    return this;
  }
}
