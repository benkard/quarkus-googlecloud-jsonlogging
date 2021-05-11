package eu.mulk.quarkus.googlecloud.jsonlogging;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import java.util.Optional;

@Recorder
public class GoogleCloudJsonLoggingRecorder {
  public RuntimeValue<Optional<java.util.logging.Formatter>> initialize() {
    return new RuntimeValue<>(Optional.of(new Formatter()));
  }
}
