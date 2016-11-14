package com.letgo.twitter.web.controllers;

import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.FetchTweetsRequest;
import com.letgo.twitter.core.api.services.TweetsFetcher;
import com.letgo.twitter.core.api.services.exceptions.InvalidFetchingRequestException;
import com.letgo.twitter.web.dto.TweetDto;
import com.letgo.twitter.web.dto.TweetsByUserDto;

import com.google.inject.Inject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class TweetsController extends Controller {

  private final TweetsFetcher tweetsFetcher;

  @Inject
  public TweetsController(TweetsFetcher tweetsFetcher) {
    this.tweetsFetcher = tweetsFetcher;
  }

  /**
   * Finds tweets from a given user.
   */
  public CompletionStage<Result> findTweets(String username, Integer pageSize) {
    FetchTweetsRequest request = new FetchTweetsRequest(username);
    request.setPageSize(pageSize);

    CompletionStage<List<Tweet>> tweetsFuture = tweetsFetcher.getTweetsByUser(request);
    return tweetsFuture
        .thenApplyAsync(this::mapToDtos)
        .thenApply(dtoTweets -> {
          TweetsByUserDto response = new TweetsByUserDto(username, dtoTweets);
          return ok(Json.toJson(response));
        })
        .exceptionally(throwable -> {
          // TODO: Make response JSON-friendly
          Throwable rootCause = ExceptionUtils.getRootCause(throwable);
          if (rootCause instanceof InvalidFetchingRequestException) {
            return badRequest(rootCause.getLocalizedMessage());
          }
          return internalServerError(rootCause.getLocalizedMessage());
        });
  }

  private List<TweetDto> mapToDtos(List<Tweet> tweets) {
    return tweets.stream()
        .map(TweetDto::fromTweet)
        .collect(Collectors.toList());
  }

}
