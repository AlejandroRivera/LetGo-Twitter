package com.letgo.twitter.web.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Singleton;

@Singleton
public class HealthController extends Controller {

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
