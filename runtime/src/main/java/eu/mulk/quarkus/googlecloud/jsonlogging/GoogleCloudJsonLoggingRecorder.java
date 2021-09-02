package eu.mulk.quarkus.googlecloud.jsonlogging;

import io.quarkus.arc.Arc;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/** A Quarkus recorder that registers {@link Formatter} as a log formatter for the application. */
@Recorder
public class GoogleCloudJsonLoggingRecorder {

  /**
   * Registers {@link Formatter} as a log formatter for the application.
   *
   * <p>Collects all discoverable {@link StructuredParameterProvider}s and {@link LabelProvider}s
   * and passes them to {@link Formatter#Formatter(Collection, Collection)}.
   *
   * @return the registered {@link Formatter}.
   */
  public RuntimeValue<Optional<java.util.logging.Formatter>> initialize() {

    var parameterProviders =
        Arc.container().select(StructuredParameterProvider.class).stream()
            .collect(Collectors.toList());

    var labelProviders =
        Arc.container().select(LabelProvider.class).stream().collect(Collectors.toList());

    return new RuntimeValue<>(Optional.of(new Formatter(parameterProviders, labelProviders)));
  }
}
