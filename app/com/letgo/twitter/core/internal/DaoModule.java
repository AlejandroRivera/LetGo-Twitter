package com.letgo.twitter.core.internal;

import com.letgo.twitter.core.api.dao.TweetsRepository;
import com.letgo.twitter.core.internal.dao.TweetsRepositoryImpl;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(DaoModule.class);

  @Override
  protected void configure() {
    bind(TweetsRepository.class).to(TweetsRepositoryImpl.class);
  }
}
