package com.letgo.twitter.core.api.dao;

import com.letgo.twitter.core.api.models.Tweet;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface TweetsRepository {
  CompletionStage<List<Tweet>> findByUsername(String username, Integer maxTweets);
}
