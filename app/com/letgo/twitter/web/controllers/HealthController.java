package com.letgo.twitter.web.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class HealthController extends Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

  public Result healthCheck() {
    JsonNode response = Json.toJson(new HealthResponse("ok"));
    return ok(response);
  }

  private static class HealthResponse {
    private String status;

    public HealthResponse(String status) {
      this.status = status;
    }

    public String getStatus() {
      return status;
    }

    public HealthResponse setStatus(String status) {
      this.status = status;
      return this;
    }
  }
}
