package com.letgo.twitter.core.api.services;

import com.letgo.twitter.core.api.models.Tweet;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface TweetsFetcher {
  CompletionStage<List<Tweet>> getTweetsByUser(FetchTweetsRequest request);
}
