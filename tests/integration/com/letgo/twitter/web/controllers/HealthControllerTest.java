package com.letgo.twitter.web.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

import com.letgo.twitter.IntegrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;

public class HealthControllerTest extends IntegrationTest {

  @Test
  public void testHealthCheck() {
    Result result = route(app, fakeRequest("GET", "/health"));
    assertThat(result.status(), equalTo(200));
    assertThat(result.contentType().get(), equalTo("application/json"));

    String responsePayload = contentAsString(result);
    JsonNode json = Json.parse(responsePayload);
    assertThat(json.get("status").asText(), equalTo("ok"));
  }
}