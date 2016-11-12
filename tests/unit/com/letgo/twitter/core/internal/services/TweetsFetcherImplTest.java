package com.letgo.twitter.core.internal.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.FetchTweetsRequest;
import com.letgo.twitter.core.api.services.TweetsFetcher;
import com.letgo.twitter.core.api.services.exceptions.FetchingException;
import com.letgo.twitter.core.api.services.exceptions.InvalidFetchingRequestException;
import com.letgo.twitter.core.internal.models.TweetPojo;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@RunWith(MockitoJUnitRunner.class)
public class TweetsFetcherImplTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private Configuration config;

  @Mock
  private TweetsRepository repository;

  @Mock
  private Validator validator;

  @InjectMocks
  private TweetsFetcherImpl tweetsFetcher;
  private HashSet<ConstraintViolation<FetchTweetsRequest>> violations;
  private ArrayList<Tweet> tweets;
  private int pageSize;
  private String username;

  @Before
  public void setUp() {
    tweets = Lists.newArrayList(new TweetPojo(RandomStringUtils.random(10), new Date()));

    pageSize = new Random().nextInt(100);
    when(config.getInt("twitter.tweets.page_size", TweetsFetcher.DEFAULT_TWEET_SIZE))
        .thenReturn(pageSize);

    violations = new HashSet<>();
    when(validator.validate(any(FetchTweetsRequest.class)))
        .thenReturn(violations);

    username = RandomStringUtils.randomAlphabetic(5);
    when(repository.findByUsername(username, pageSize))
        .thenReturn(CompletableFuture.completedFuture(tweets));
  }

  @Test
  public void testNormalExecution() throws ExecutionException, InterruptedException {
    FetchTweetsRequest request = new FetchTweetsRequest(username);
    CompletionStage<List<Tweet>> tweetsByUser = tweetsFetcher.getTweetsByUser(request);
    List<Tweet> tweets = tweetsByUser.toCompletableFuture().get();
    assertThat(tweets, equalTo(tweets));
  }

  @Test
  public void testValidationError() throws Throwable {
    violations.add(mock(ConstraintViolation.class));
    expectedException.expect(FetchingException.class);
    expectedException.expectCause(instanceOf(InvalidFetchingRequestException.class));

    FetchTweetsRequest request = new FetchTweetsRequest(username);
    CompletionStage<List<Tweet>> tweetsByUser = tweetsFetcher.getTweetsByUser(request);
    CompletableFuture<List<Tweet>> completableFuture = tweetsByUser.toCompletableFuture();
    try {
      completableFuture.get();
      fail("expected exception to be thrown");
    } catch (ExecutionException e) {
      throw e.getCause();
    }
  }

  @Test
  public void testRepositoryError() throws Throwable {
    RuntimeException runtimeException = new RuntimeException("fake runtime exception");

    expectedException.expect(FetchingException.class);
    expectedException.expectCause(equalTo(runtimeException));

    when(repository.findByUsername(anyString(), anyInt()))
        .thenThrow(runtimeException);

    FetchTweetsRequest request = new FetchTweetsRequest(username);
    CompletionStage<List<Tweet>> tweetsByUser = tweetsFetcher.getTweetsByUser(request);
    CompletableFuture<List<Tweet>> completableFuture = tweetsByUser.toCompletableFuture();
    try {
      completableFuture.get();
      fail("expected exception to be thrown");
    } catch (ExecutionException e) {
      throw e.getCause();
    }
  }
}