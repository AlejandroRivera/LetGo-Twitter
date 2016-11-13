package com.letgo.twitter;

import com.google.inject.Module;
import play.Application;
import play.Environment;
import play.Mode;
import play.api.inject.Binding;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class IntegrationTest extends WithApplication {

  @Override
  protected Application provideApplication() {
    Map<String, Object> extraConfig = getExtraConfig();

    return new GuiceApplicationBuilder()
        .in(new Environment(new File("."), getClass().getClassLoader(), Mode.TEST))
        .configure(extraConfig)
        .overrides(bindings())
        .overrides(modules())
        .build();
  }

  private Map<String, Object> getExtraConfig() {
    return new HashMap<>(0);
  }

  protected Binding<?>[] bindings() {
    return new Binding<?>[0];
  }

  protected Module[] modules() {
    return new Module[0];
  }
}
