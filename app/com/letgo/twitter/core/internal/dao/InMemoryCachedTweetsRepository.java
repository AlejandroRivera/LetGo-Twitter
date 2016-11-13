package com.letgo.twitter.core.internal.dao;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.api.models.Tweet;

import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * A repository wrapper that adds in-memory caching capabilities.
 */
public class InMemoryCachedTweetsRepository implements TweetsRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCachedTweetsRepository.class);

  private final TweetsRepository delegate;

  private final Cache<String, List<Tweet>> cache;

  /**
   * Constructor that relies on configuration.
   */
  public InMemoryCachedTweetsRepository(TweetsRepository delegate, Configuration configuration) {
    this(delegate,
        CacheBuilder.newBuilder()
            .maximumSize(configuration.getInt("twitter.cache.max_users", 100))
            .expireAfterWrite(configuration.getMilliseconds("twitter.cache.expire_after", 30_000L), TimeUnit.MILLISECONDS)
            .removalListener(new RemovalListener())
            .build());
  }

  /**
   * Constructor useful for testing.
   */
  public InMemoryCachedTweetsRepository(TweetsRepository delegate, Cache<String, List<Tweet>> cache) {
    this.delegate = delegate;
    this.cache = cache;
  }

  @Override
  public CompletionStage<List<Tweet>> findByUsername(String username, Integer maxTweets) {
    List<Tweet> tweets = cache.getIfPresent(username);
    if (tweets != null) {
      LOGGER.info("Cache hit for user '{}'! Cached tweets: {}", username, tweets.size());
      if (tweets.size() >= maxTweets) {
        return CompletableFuture.completedFuture(
            Lists.newArrayList(
                tweets.subList(0, maxTweets)));
      } else {
        LOGGER.warn("Requested tweets ({}) exceed those cached ({}) for user: {}", maxTweets, tweets.size(), username);
      }
    }

    LOGGER.info("Cache miss. Getting fresh tweets for user: '{}'...", username);
    CompletionStage<List<Tweet>> tweetsFuture = delegate.findByUsername(username, maxTweets);
    tweetsFuture.thenAccept(updatedTweets -> {
      cache.put(username, updatedTweets);
      LOGGER.debug("Caching {} fresh tweets for user: '{}'", updatedTweets.size(), username);
    });

    return tweetsFuture;
  }

  private static class RemovalListener implements com.google.common.cache.RemovalListener<String, List<Tweet>> {
    @Override
    public void onRemoval(RemovalNotification<String, List<Tweet>> notification) {
      String username = notification.getKey() == null ? "unknown" : notification.getKey();
      List<Tweet> value = notification.getValue();
      String tweetsExpired = value == null ? "unknown" : String.valueOf(value.size());
      LOGGER.debug("Expired {} cached tweet(s) for user: {}", tweetsExpired, username);
    }
  }
}
