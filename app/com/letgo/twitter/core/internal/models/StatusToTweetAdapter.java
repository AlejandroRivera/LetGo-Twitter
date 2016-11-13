package com.letgo.twitter.core.internal.models;

import com.letgo.twitter.core.api.models.Tweet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import twitter4j.Status;

import java.util.Date;

/**
 * Adapts the Tweet object ({@link Status}) from Twitter4J into our own.
 */
public class StatusToTweetAdapter implements Tweet {

  private final Status status;

  /**
   * Default constructor.
   */
  public StatusToTweetAdapter(Status status) {
    if (status == null) {
      throw new IllegalArgumentException("Status object must not be null");
    }
    this.status = status;
  }

  @Override
  public String getMessage() {
    return status.getText();
  }

  @Override
  public Date getCreatedAt() {
    return new Date(status.getCreatedAt().getTime());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("message", getMessage())
        .append("date", getCreatedAt())
        .toString();
  }
}
