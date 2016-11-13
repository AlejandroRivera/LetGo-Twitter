package com.letgo.twitter.core.internal.dao;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.exceptions.FetchingException;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class Twitter4jRepositoryTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private Twitter twitter;
  @Mock
  private QueryResult queryResult;
  @Mock
  private Status status;
  @Captor
  private ArgumentCaptor<Query> queryArgumentCaptor;
  @InjectMocks
  private Twitter4jRepository repository;

  private String username;
  private int maxTweets;

  @Before
  public void setUp() {
    username = RandomStringUtils.randomAlphabetic(5);
    maxTweets = new Random().nextInt(100);
  }

  @Test
  public void testFetching() throws TwitterException, ExecutionException, InterruptedException {
    when(twitter.search(queryArgumentCaptor.capture()))
        .thenReturn(queryResult);
    when(queryResult.getTweets())
        .thenReturn(Lists.newArrayList(status));
    when(status.getText()).thenReturn(RandomStringUtils.random(10));
    when(status.getCreatedAt()).thenReturn(new Date());

    CompletionStage<List<Tweet>> tweetsFuture = repository.findByUsername(username, maxTweets);
    List<Tweet> tweets = tweetsFuture.toCompletableFuture().get();
    assertThat(tweets.size(), equalTo(1));
    assertThat(tweets.get(0).getMessage(), equalTo(status.getText()));
    assertThat(tweets.get(0).getCreatedAt(), equalTo(status.getCreatedAt()));

    Query query = queryArgumentCaptor.getValue();
    assertThat(query.getQuery(), containsString(username));
    assertThat(query.getCount(), equalTo(maxTweets));
  }

  @Test
  public void testErrorFetching() throws Throwable {
    TwitterException twitterException = new TwitterException("uh-oh!");
    when(twitter.search(queryArgumentCaptor.capture()))
        .thenThrow(twitterException);

    CompletionStage<List<Tweet>> tweetsFuture = repository.findByUsername(username, maxTweets);

    expectedException.expect(FetchingException.class);
    expectedException.expectCause(equalTo(twitterException));

    try {
      tweetsFuture.toCompletableFuture().get();
      fail("Exception should have been raised!");
    } catch (ExecutionException e) {
      throw e.getCause();
    }
  }
}