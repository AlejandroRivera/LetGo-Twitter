package com.letgo.twitter.core.internal.dao;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.api.models.Tweet;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class TweetsRepositoryImpl implements TweetsRepository {

  public CompletionStage<List<Tweet>> findByUsername(String username, Integer maxTweets) {
    throw new RuntimeException("Not implemented yet");
  }
}
