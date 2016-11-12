package com.letgo.twitter.web.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static play.test.Helpers.contentAsString;

import com.letgo.twitter.core.api.models.Tweet;
import com.letgo.twitter.core.api.services.FetchTweetsRequest;
import com.letgo.twitter.core.api.services.TweetsFetcher;
import com.letgo.twitter.core.internal.models.TweetPojo;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.api.inject.Binding;
import play.inject.Bindings;
import play.mvc.Result;
import play.test.Helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@RunWith(MockitoJUnitRunner.class)
public class TweetsControllerTest extends IntegrationTest {

  @Mock
  private TweetsFetcher tweetsFetcher;

  @Captor
  private ArgumentCaptor<FetchTweetsRequest> requestCaptor;

  private ArrayList<Tweet> tweets;

  @Override
  protected Binding<?>[] bindings() {
    return new Binding[]{
        Bindings.bind(TweetsFetcher.class).toInstance(tweetsFetcher)
    };
  }

  @Before
  public void setUp() {
    tweets = Lists.newArrayList(
        new TweetPojo(RandomStringUtils.random(10), new Date()),
        new TweetPojo(RandomStringUtils.random(5), new Date()));

    when(tweetsFetcher.getTweetsByUser(any()))
        .thenReturn(CompletableFuture.completedFuture(tweets));
  }

  @Test
  public void testRequest() {
    String username = RandomStringUtils.randomAlphabetic(5);
    int pageSize = new Random().nextInt(100);
    Result result = Helpers.route(app, Helpers.fakeRequest("GET", "/twitter/" + username + "/tweets?page_size=" + pageSize));
    assertThat(result.status(), equalTo(200));
    assertThat(result.contentType().get(), equalTo("application/json"));

    assertThat(contentAsString(result), containsString(username));
    tweets.forEach(
        tweet -> assertThat(contentAsString(result), containsString(tweet.getMessage()))
    );

    verify(tweetsFetcher).getTweetsByUser(requestCaptor.capture());
    FetchTweetsRequest request = requestCaptor.getValue();
    assertThat(request.getUsername(), equalTo(username));
    assertThat(request.getPageSize().isPresent(), equalTo(true));
    assertThat(request.getPageSize().get(), equalTo(pageSize));
  }
}