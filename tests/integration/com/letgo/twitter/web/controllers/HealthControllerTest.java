package com.letgo.twitter.web.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.contentAsString;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;
import play.test.WithApplication;

public class HealthControllerTest extends WithApplication {

  @Test
  public void testHealthCheck() {
    Result result = new HealthController().healthCheck();
    assertThat(result.status(), equalTo(200));
    assertThat(result.contentType().get(), equalTo("application/json"));

    String responsePayload = contentAsString(result);
    JsonNode json = Json.parse(responsePayload);
    assertThat(json.get("status").asText(), equalTo("ok"));
  }
}