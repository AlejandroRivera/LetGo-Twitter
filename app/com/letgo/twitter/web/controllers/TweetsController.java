package com.letgo.twitter.web.controllers;

import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.FetchTweetsRequest;
import com.letgo.twitter.core.api.services.TweetsFetcher;
import com.letgo.twitter.web.dto.TweetsByUserDto;

import com.google.inject.Inject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletionStage;
import javax.inject.Singleton;

@Singleton
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
    return tweetsFuture.thenApply(
        tweets -> {
          TweetsByUserDto response = new TweetsByUserDto(username, tweets);
          return ok(Json.toJson(response));
        }
    );
  }

}
