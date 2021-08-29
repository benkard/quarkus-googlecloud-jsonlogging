package eu.mulk.quarkus.googlecloud.jsonlogging;

import io.quarkus.arc.Arc;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import java.util.Optional;
import java.util.stream.Collectors;

@Recorder
public class GoogleCloudJsonLoggingRecorder {
  public RuntimeValue<Optional<java.util.logging.Formatter>> initialize() {
    var parameterProviders =
        Arc.container().select(ParameterProvider.class).stream().collect(Collectors.toList());
    return new RuntimeValue<>(Optional.of(new Formatter(parameterProviders)));
  }
}
