package eu.mulk.quarkus.googlecloud.jsonlogging;

import io.smallrye.common.constraint.Nullable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * A JSON log entry compatible with Google Cloud Logging.
 *
 * <p>Roughly (but not quite) corresponds to Google Cloud Logging's <a
 * href="https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry">LogEntry</a>
 * structure.
 *
 * <p>A few of the fields are treated specially by the fluentd instance running in Google Kubernetes
 * Engine. All other fields end up in the jsonPayload field on the Google Cloud Logging side.
 */
final class LogEntry {

  private final String message;
  private final String severity;
  private final Timestamp timestamp;
  @Nullable private final String trace;
  @Nullable private final String spanId;
  private final SourceLocation sourceLocation;
  private final Map<String, String> labels;
  private final List<StructuredParameter> parameters;
  private final Map<String, String> mappedDiagnosticContext;
  @Nullable private final String nestedDiagnosticContext;
  @Nullable private final String type;

  LogEntry(
      String message,
      String severity,
      Timestamp timestamp,
      @Nullable String trace,
      @Nullable String spanId,
      SourceLocation sourceLocation,
      Map<String, String> labels,
      List<StructuredParameter> parameters,
      Map<String, String> mappedDiagnosticContext,
      @Nullable String nestedDiagnosticContext,
      @Nullable String type) {
    this.message = message;
    this.severity = severity;
    this.timestamp = timestamp;
    this.trace = trace;
    this.spanId = spanId;
    this.sourceLocation = sourceLocation;
    this.labels = labels;
    this.parameters = parameters;
    this.mappedDiagnosticContext = mappedDiagnosticContext;
    this.nestedDiagnosticContext = nestedDiagnosticContext;
    this.type = type;
  }

  static final class SourceLocation {

    @Nullable private final String file;
    @Nullable private final String line;
    @Nullable private final String function;

    SourceLocation(@Nullable String file, @Nullable String line, @Nullable String function) {
      this.file = file;
      this.line = line;
      this.function = function;
    }

    JsonObject json() {
      return Json.createObjectBuilder()
          .add("file", file)
          .add("line", line)
          .add("function", function)
          .build();
    }
  }

  static final class Timestamp {

    private final long seconds;
    private final int nanos;

    Timestamp(long seconds, int nanos) {
      this.seconds = seconds;
      this.nanos = nanos;
    }

    Timestamp(Instant t) {
      this(t.getEpochSecond(), t.getNano());
    }

    JsonObject json() {
      return Json.createObjectBuilder().add("seconds", seconds).add("nanos", nanos).build();
    }
  }

  JsonObjectBuilder json() {
    var b = Json.createObjectBuilder();

    if (trace != null) {
      b.add("trace", trace);
    }

    if (spanId != null) {
      b.add("spanId", spanId);
    }

    if (nestedDiagnosticContext != null && !nestedDiagnosticContext.isEmpty()) {
      b.add("nestedDiagnosticContext", nestedDiagnosticContext);
    }

    if (!labels.isEmpty()) {
      b.add("labels", jsonOfStringMap(labels));
    }

    if (type != null) {
      b.add("@type", type);
    }

    return b.add("message", message)
        .add("severity", severity)
        .add("timestamp", timestamp.json())
        .add("sourceLocation", sourceLocation.json())
        .addAll(jsonOfStringMap(mappedDiagnosticContext))
        .addAll(jsonOfParameterMap(parameters));
  }

  private static JsonObjectBuilder jsonOfStringMap(Map<String, String> stringMap) {
    return stringMap.entrySet().stream()
        .reduce(
            Json.createObjectBuilder(),
            (acc, x) -> acc.add(x.getKey(), x.getValue()),
            JsonObjectBuilder::addAll);
  }

  private static JsonObjectBuilder jsonOfParameterMap(List<StructuredParameter> parameters) {
    return parameters.stream()
        .reduce(
            Json.createObjectBuilder(),
            (acc, p) -> acc.addAll(p.json()),
            JsonObjectBuilder::addAll);
  }
}
