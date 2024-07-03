// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import io.smallrye.common.constraint.Nullable;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * A JSON log entry compatible with Google Cloud Logging.
 *
 * <p>Roughly (but not quite) corresponds to Google Cloud Logging's <a
 * href="https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry">LogEntry</a>
 * structure.
 *
 * <p>A few of the fields are <a href="https://cloud.google.com/logging/docs/structured-logging">
 * treated specially</a> by the fluentd instance running in Google Kubernetes Engine. All other
 * fields end up in the jsonPayload field on the Google Cloud Logging side.
 */
final class LogEntry {

  private final String message;
  private final String severity;
  private final Timestamp timestamp;
  @Nullable private final String trace;
  @Nullable private final String spanId;
  @Nullable private final SourceLocation sourceLocation;
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
      @Nullable SourceLocation sourceLocation,
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

    void json(StringBuilder b) {
      var commaNeeded = false;

      if (file != null) {
        b.append("\"file\":");
        appendEscapedString(b, file);
        commaNeeded = true;
      }

      if (line != null) {
        if (commaNeeded) {
          b.append(",");
        }
        b.append("\"line\":");
        appendEscapedString(b, line);
        commaNeeded = true;
      }

      if (function != null) {
        if (commaNeeded) {
          b.append(",");
        }
        b.append("\"function\":");
        appendEscapedString(b, function);
      }
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

    void json(StringBuilder b) {
      b.append("\"seconds\":");
      b.append(seconds);
      b.append(",\"nanos\":");
      b.append(nanos);
    }
  }

  void json(StringBuilder b) {
    if (trace != null) {
      b.append("\"logging.googleapis.com/trace\":");
      appendEscapedString(b, trace);
      b.append(",");
    }

    if (spanId != null) {
      b.append("\"logging.googleapis.com/spanId\":");
      appendEscapedString(b, spanId);
      b.append(",");
    }

    if (nestedDiagnosticContext != null && !nestedDiagnosticContext.isEmpty()) {
      b.append("\"nestedDiagnosticContext\":");
      appendEscapedString(b, nestedDiagnosticContext);
      b.append(",");
    }

    if (!labels.isEmpty()) {
      b.append("\"logging.googleapis.com/labels\":{");

      var first = true;
      for (var entry : labels.entrySet()) {
        if (!first) {
          b.append(",");
        } else {
          first = false;
        }

        appendEscapedString(b, entry.getKey());
        b.append(":");
        appendEscapedString(b, entry.getValue());
      }

      b.append("},");
    }

    for (var entry : mappedDiagnosticContext.entrySet()) {
      appendEscapedString(b, entry.getKey());
      b.append(":");
      appendEscapedString(b, entry.getValue());
      b.append(",");
    }

    for (var parameter : parameters) {
      var jsonObject = parameter.json().build();
      jsonObject.forEach(
          (key, value) -> {
            appendEscapedString(b, key);
            b.append(":");
            appendJsonObject(b, value);
            b.append(",");
          });
    }

    if (type != null) {
      b.append("\"@type\":");
      appendEscapedString(b, type);
      b.append(",");
    }

    if (sourceLocation != null) {
      b.append("\"logging.googleapis.com/sourceLocation\":{");
      sourceLocation.json(b);
      b.append("},");
    }

    b.append("\"message\":");
    appendEscapedString(b, message);

    b.append(",\"severity\":");
    appendEscapedString(b, severity);

    b.append(",\"timestamp\":{");
    timestamp.json(b);
    b.append("}");
  }

  private void appendJsonObject(StringBuilder b, JsonValue value) {
    switch (value.getValueType()) {
      case ARRAY:
        b.append("[");
        var array = value.asJsonArray();
        for (var i = 0; i < array.size(); i++) {
          if (i > 0) {
            b.append(",");
          }
          appendJsonObject(b, array.get(i));
        }
        b.append("]");
        break;

      case OBJECT:
        b.append("{");
        var object = value.asJsonObject();
        var first = true;
        for (var entry : object.entrySet()) {
          if (!first) {
            b.append(",");
          } else {
            first = false;
          }
          appendEscapedString(b, entry.getKey());
          b.append(":");
          appendJsonObject(b, entry.getValue());
        }
        b.append("}");
        break;

      case STRING:
        appendEscapedString(b, ((JsonString) value).getString());
        break;

      case NUMBER:
        b.append(value);
        break;

      case TRUE:
        b.append("true");
        break;

      case FALSE:
        b.append("false");
        break;

      case NULL:
        b.append("null");
        break;
    }
  }

  private static void appendEscapedString(StringBuilder b, String s) {
    b.append('"');

    for (var i = 0; i < s.length(); i++) {
      var c = s.charAt(i);

      switch (c) {
        case '"':
          b.append("\\\"");
          break;

        case '\\':
          b.append("\\\\");
          break;

        case '\b':
          b.append("\\b");
          break;

        case '\f':
          b.append("\\f");
          break;

        case '\n':
          b.append("\\n");
          break;

        case '\r':
          b.append("\\r");
          break;

        case '\t':
          b.append("\\t");
          break;

        default:
          if (c < 0x20) {
            b.append("\\u00");
            b.append(Character.forDigit((c >> 4) & 0xf, 16));
            b.append(Character.forDigit(c & 0xf, 16));
          } else {
            b.append(c);
          }
      }
    }

    b.append('"');
  }
}
