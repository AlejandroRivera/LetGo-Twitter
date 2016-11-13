package com.letgo.twitter.core.internal;

import com.letgo.twitter.core.api.services.TweetsFetcher;
import com.letgo.twitter.core.internal.services.TweetsFetcherImpl;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicesModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServicesModule.class);

  @Override
  protected void configure() {
    bind(TweetsFetcher.class).to(TweetsFetcherImpl.class);
  }

}
