package com.letgo.twitter.core.api.services;

import com.letgo.twitter.core.api.models.Tweet;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface TweetsFetcher {

  int DEFAULT_TWEET_SIZE = 10;

  CompletionStage<List<Tweet>> getTweetsByUser(FetchTweetsRequest request);
}
