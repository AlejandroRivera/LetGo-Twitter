package com.letgo.twitter.web.dto;

import java.util.List;

public class TweetsByUserDto {
  private String username;
  private List<TweetDto> tweets;

  public TweetsByUserDto(String username, List<TweetDto> tweets) {
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

  public List<TweetDto> getTweets() {
    return tweets;
  }

  public TweetsByUserDto setTweets(List<TweetDto> tweets) {
    this.tweets = tweets;
    return this;
  }
}
