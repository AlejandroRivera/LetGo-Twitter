package com.letgo.twitter.core.internal.dao;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.exceptions.FetchingException;
import com.letgo.twitter.core.internal.models.StatusToTweetAdapter;

import com.google.inject.Inject;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class Twitter4jRepository implements TweetsRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(Twitter4jRepository.class);

  private Twitter twitter;

  @Inject
  public Twitter4jRepository(Twitter twitter) {
    this.twitter = twitter;
  }

  /**
   * Fetches tweets using Search API exposed through Twitter4J.
   *
   * @see Twitter#search(Query) Twitter4J Search API
   * @see Query Twitter4J Query object
   */
  public CompletionStage<List<Tweet>> findByUsername(String username, Integer maxTweets) {
    return CompletableFuture.supplyAsync(() -> {
      // TODO: confirm what exactly we want to retrieve: tweets by user, tweets mentioning user, re-tweets?
      Query query = new Query(username).count(maxTweets);
      LOGGER.info("Searching Twitter for: {}", query);
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      try {
        QueryResult result = twitter.search(query);
        stopWatch.stop();
        return result.getTweets().stream()
            .map(StatusToTweetAdapter::new)
            .collect(Collectors.toList());
      } catch (TwitterException e) {
        throw new FetchingException("Error fetching tweets with query: " + query, e);
      } finally {
        stopWatch.getTime();
        LOGGER.info("Finished querying Twitter in {}ms", stopWatch.getTime());
      }
    });
  }
}
