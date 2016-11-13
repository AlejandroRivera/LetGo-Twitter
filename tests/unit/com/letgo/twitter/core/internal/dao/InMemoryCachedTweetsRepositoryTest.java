package com.letgo.twitter.core.internal.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.api.models.Tweet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryCachedTweetsRepositoryTest {

  @Mock
  private TweetsRepository mockRepo;
  private Cache<String, List<Tweet>> cache;
  private InMemoryCachedTweetsRepository cachedRepo;
  private String username;
  private ArrayList<Tweet> tweets;

  @Before
  public void setUp() {
    cache = CacheBuilder.newBuilder().build();
    cachedRepo = new InMemoryCachedTweetsRepository(mockRepo, cache);
    username = RandomStringUtils.randomAlphanumeric(5);
    tweets = Lists.newArrayList();
  }

  @Test
  public void cacheMiss() throws ExecutionException, InterruptedException {
    when(mockRepo.findByUsername(username, 1))
        .thenReturn(CompletableFuture.completedFuture(tweets));

    List<Tweet> retrievedTweets = cachedRepo.findByUsername(username, 1).toCompletableFuture().get();
    assertThat(retrievedTweets, equalTo(tweets));
    assertThat(cache.getIfPresent(username), notNullValue());
  }

  @Test
  public void cacheHitExact() throws ExecutionException, InterruptedException {
    tweets.add(mock(Tweet.class));
    tweets.add(mock(Tweet.class));
    cache.put(username, tweets);

    when(mockRepo.findByUsername(username, 1))
        .thenReturn(CompletableFuture.completedFuture(tweets));

    List<Tweet> retrievedTweets = cachedRepo.findByUsername(username, tweets.size()).toCompletableFuture().get();
    assertThat(retrievedTweets, equalTo(tweets));

    verify(mockRepo, never()).findByUsername(anyString(), anyInt());
  }

  @Test
  public void cacheHitLessThan() throws ExecutionException, InterruptedException {
    tweets.add(mock(Tweet.class));
    tweets.add(mock(Tweet.class));
    cache.put(username, tweets);

    when(mockRepo.findByUsername(username, 1))
        .thenReturn(CompletableFuture.completedFuture(tweets));

    List<Tweet> retrievedTweets = cachedRepo.findByUsername(username, tweets.size() - 1).toCompletableFuture().get();
    assertThat(retrievedTweets, equalTo(tweets.subList(0, tweets.size() - 1)));

    verify(mockRepo, never()).findByUsername(anyString(), anyInt());
  }


  @Test
  public void cacheHitMoreThan() throws ExecutionException, InterruptedException {
    tweets.add(mock(Tweet.class));
    tweets.add(mock(Tweet.class));
    cache.put(username, tweets);

    when(mockRepo.findByUsername(username, tweets.size() + 1))
        .thenReturn(CompletableFuture.completedFuture(tweets));

    List<Tweet> retrievedTweets = cachedRepo.findByUsername(username, tweets.size() + 1).toCompletableFuture().get();

    assertThat(retrievedTweets, equalTo(tweets));

    verify(mockRepo, atLeastOnce()).findByUsername(username, tweets.size() + 1);
  }
}