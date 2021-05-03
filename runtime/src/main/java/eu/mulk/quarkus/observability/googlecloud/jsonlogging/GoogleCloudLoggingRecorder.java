package eu.mulk.quarkus.observability.googlecloud.jsonlogging;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import java.util.Optional;
import java.util.logging.Formatter;
import javax.json.bind.spi.JsonbProvider;

@Recorder
public class GoogleCloudLoggingRecorder {

  public RuntimeValue<Optional<Formatter>> initialize() {
    var jsonb = JsonbProvider.provider().create().build();
    return new RuntimeValue<>(Optional.of(new GoogleCloudLoggingFormatter(jsonb)));
  }
}
