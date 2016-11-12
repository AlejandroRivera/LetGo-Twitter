package com.letgo.twitter.core.internal.services;

import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.FetchTweetsRequest;
import com.letgo.twitter.core.api.services.TweetsFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class TweetsFetcherImpl implements TweetsFetcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(TweetsFetcherImpl.class);

  @Override
  public CompletionStage<List<Tweet>> getTweetsByUser(FetchTweetsRequest request) {
    throw new RuntimeException("Not implemented yet");
  }
}
