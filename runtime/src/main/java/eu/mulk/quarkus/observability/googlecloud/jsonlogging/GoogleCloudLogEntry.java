package eu.mulk.quarkus.observability.googlecloud.jsonlogging;

import io.smallrye.common.constraint.Nullable;
import java.time.Instant;
import java.util.Map;
import javax.json.bind.annotation.JsonbProperty;

/**
 * A JSON log entry compatible with Google Cloud Logging.
 *
 * <p>Roughly (but not quite) corresponds to Google Cloud Logging's
 * <a href="https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry">LogEntry</a>
 * structure.
 */
public record GoogleCloudLogEntry(
    String getMessage,
    String getSeverity,
    Timestamp getTimestamp,
    @Nullable String getTrace,
    @Nullable String getSpanId,
    @Nullable SourceLocation getSourceLocation,
    @Nullable Map<String, String> getLabels,
    @Nullable Map<String, Object> getParameters,
    @Nullable Map<String, String> getMappedDiagnosticContext,
    @Nullable String getNestedDiagnosticContext,
    @Nullable @JsonbProperty("@type") String getType) {

  static public record SourceLocation(
      @Nullable String getFile,
      @Nullable String getLine,
      @Nullable String getFunction) {}

  static public record Timestamp(
      long getSeconds,
      int getNanos) {

    public Timestamp(Instant t) {
      this(t.getEpochSecond(), t.getNano());
    }
  }
}
