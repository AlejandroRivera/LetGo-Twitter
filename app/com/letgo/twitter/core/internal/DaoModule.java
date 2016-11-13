package com.letgo.twitter.core.internal;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.internal.dao.InMemoryCachedTweetsRepository;
import com.letgo.twitter.core.internal.dao.Twitter4jRepository;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class DaoModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TweetsRepository.class).toProvider(TweetsRepositoryProvider.class);
    bind(Twitter.class).toProvider(TwitterClientProvider.class);
  }

  public static class TwitterClientProvider implements Provider<Twitter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoModule.class);

    private static final String ACCESS_TOKEN_KEY = "twitter.client_app.access_token.token";
    private static final String ACCESS_SECRET_KEY = "twitter.client_app.access_token.secret";
    private static final String CONSUMER_KEY = "twitter.client_app.consumer.key";
    private static final String CONSUMER_SECRET = "twitter.client_app.consumer.secret";

    private final Twitter twitter;

    /**
     * Configures a Twitter client with the appropriate OAuth setup.
     */
    @Inject
    public TwitterClientProvider(Configuration config) {
      twitter = TwitterFactory.getSingleton();
      setOauthConsumer(config, twitter);
      setAccessToken(config, twitter);

      LOGGER.info("Twitter4J client initialized.");
    }

    private void setOauthConsumer(Configuration configuration, Twitter twitter) {
      String consumerKey = configuration.getString(CONSUMER_KEY);
      String consumerSecret = configuration.getString(CONSUMER_SECRET);

      if (StringUtils.isEmpty(consumerKey) || StringUtils.isEmpty(consumerSecret)) {
        throw new ProvisionException("Cannot create Twitter client due to missing configuration.");
      }
      twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    private void setAccessToken(Configuration configuration, Twitter twitter) {
      String accessToken = configuration.getString(ACCESS_TOKEN_KEY);
      String accessSecret = configuration.getString(ACCESS_SECRET_KEY);
      if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(accessSecret)) {
        throw new ProvisionException("Twitter Access token/secret is missing!");
      }
      twitter.setOAuthAccessToken(new AccessToken(accessToken, accessSecret));
    }

    @Override
    public Twitter get() {
      return twitter;
    }

  }

  public static class TweetsRepositoryProvider implements Provider<TweetsRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TweetsRepositoryProvider.class);

    private final InMemoryCachedTweetsRepository cachedRepository;

    @Inject
    public TweetsRepositoryProvider(Twitter4jRepository twitter4jRepository, Configuration configuration) {
      this.cachedRepository = new InMemoryCachedTweetsRepository(twitter4jRepository, configuration);
      LOGGER.info("Tweet caching will be performed by: {}", cachedRepository.getClass().getSimpleName());
    }

    @Override
    public TweetsRepository get() {
      return cachedRepository;
    }
  }
}
