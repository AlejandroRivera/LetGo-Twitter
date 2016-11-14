package com.letgo.twitter.core.api.services;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import java.util.Optional;

public class FetchTweetsRequest {

  @NotEmpty
  private String username;
  @Range(min = 1, max = 100)
  private Integer pageSize;

  public FetchTweetsRequest(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public FetchTweetsRequest setUsername(String username) {
    this.username = username;
    return this;
  }

  public Optional<Integer> getPageSize() {
    return Optional.ofNullable(pageSize);
  }

  public FetchTweetsRequest setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }
}
