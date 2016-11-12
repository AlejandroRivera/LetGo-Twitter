package com.letgo.twitter.core.internal.services;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.FetchTweetsRequest;
import com.letgo.twitter.core.api.services.TweetsFetcher;
import com.letgo.twitter.core.api.services.exceptions.FetchingException;
import com.letgo.twitter.core.api.services.exceptions.InvalidFetchingRequestException;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class TweetsFetcherImpl implements TweetsFetcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(TweetsFetcherImpl.class);

  private final Configuration config;
  private final Validator validator;

  private TweetsRepository repository;

  /**
   * Default constructor.
   */
  @Inject
  public TweetsFetcherImpl(Configuration config,
                           TweetsRepository repository,
                           Validator validator) {
    this.repository = repository;
    this.config = config;
    this.validator = validator;
  }

  @Override
  public CompletionStage<List<Tweet>> getTweetsByUser(FetchTweetsRequest request) {
    return CompletableFuture
        .supplyAsync(() -> validate(request))
        .thenCompose(validatedRequest ->
            repository.findByUsername(
                validatedRequest.getUsername(),
                validatedRequest.getPageSize().orElse(getDefaultPageSize()))
        )
        .exceptionally(throwable -> {
          throw new FetchingException("Could not fetch tweets for user: " + request.getUsername(), throwable.getCause());
        });
  }

  private FetchTweetsRequest validate(FetchTweetsRequest request) {
    Set<ConstraintViolation<FetchTweetsRequest>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new InvalidFetchingRequestException("Invalid request", request, violations);
    } else {
      return request;
    }

  }

  private int getDefaultPageSize() {
    return config.getInt("twitter.tweets.page_size", DEFAULT_TWEET_SIZE);
  }

}
