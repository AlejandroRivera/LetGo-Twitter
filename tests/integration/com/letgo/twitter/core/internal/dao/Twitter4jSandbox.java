package com.letgo.twitter.core.internal.dao;

import com.letgo.twitter.core.internal.models.StatusToTweetAdapter;

import org.junit.Ignore;
import org.junit.Test;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This is just to test locally to explore the Twitter4j library.
 */
@Ignore
public class Twitter4jSandbox {

  @Test
  public void testFetchingTweets() throws TwitterException {
    Twitter twitter = TwitterFactory.getSingleton();
    twitter.setOAuthConsumer("ZkKbEpAy6R4OhpFGKsH0kesBA", "3Mp03x4P9Deyu066tRuYI9ow1Vw3XKpQbrvruouFjkff07220U");
    twitter.setOAuthAccessToken(
        new AccessToken("18919854-zwp0v2c3tTPLk91LjeH5EOvasrWivYizRstMyHO7k", "Yr6v8pyT9dxE2FNo0wLybG1WrgHiKH7ABVlXdojGAprzc"));
    Query query = new Query("letgo").count(10);
    QueryResult result = twitter.search(query);
    List<StatusToTweetAdapter> tweets = result.getTweets().stream()
        .map(StatusToTweetAdapter::new)
        .collect(Collectors.toList());

    System.out.println(tweets);

  }
}