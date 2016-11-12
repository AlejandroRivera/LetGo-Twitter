package com.letgo.twitter.core.api.services.exceptions;

import com.letgo.twitter.core.api.services.FetchTweetsRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class InvalidFetchingRequestException extends ConstraintViolationException {

  private final FetchTweetsRequest request;

  public InvalidFetchingRequestException(String msg,
                                         FetchTweetsRequest request,
                                         Set<ConstraintViolation<FetchTweetsRequest>> violations) {
    super(msg, violations);
    this.request = request;
  }

  public FetchTweetsRequest getRequest() {
    return request;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    return builder
        .append("violations", this.getConstraintViolations())
        .toString();
  }
}
