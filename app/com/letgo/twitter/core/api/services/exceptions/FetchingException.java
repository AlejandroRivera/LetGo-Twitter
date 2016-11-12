package com.letgo.twitter.core.api.services.exceptions;

public class FetchingException extends RuntimeException {
  public FetchingException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
