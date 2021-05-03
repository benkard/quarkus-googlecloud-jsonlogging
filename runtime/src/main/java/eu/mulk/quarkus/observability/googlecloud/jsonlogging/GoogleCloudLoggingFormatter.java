package eu.mulk.quarkus.observability.googlecloud.jsonlogging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Level;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;
import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

/**
 * Formats log records as JSON for consumption by Google Cloud Logging.
 *
 * <p>Meant to be used in containers running on Google Kubernetes Engine (GKE).
 *
 * @see GoogleCloudLogEntry
 */
class GoogleCloudLoggingFormatter extends ExtFormatter {

  private static final String TRACE_LEVEL = "TRACE";
  private static final String DEBUG_LEVEL = "DEBUG";
  private static final String INFO_LEVEL = "INFO";
  private static final String WARNING_LEVEL = "WARNING";
  private static final String ERROR_LEVEL = "ERROR";

  private static final String ERROR_EVENT_TYPE =
      "type.googleapis.com/google.devtools.clouderrorreporting.v1beta1.ReportedErrorEvent";

  private final Jsonb jsonb;

  GoogleCloudLoggingFormatter(Jsonb jsonb) {
    this.jsonb = jsonb;
  }

  @Override
  public String format(ExtLogRecord logRecord) {
    var message = formatMessageWithStackTrace(logRecord);

    var parameters = new HashMap<String, Object>();
    var labels = new HashMap<String, String>();
    if (logRecord.getParameters() != null) {
      for (var parameter : logRecord.getParameters()) {
        if (parameter instanceof KeyValueParameter kvparam) {
          parameters.put(kvparam.key(), kvparam.value());
        } else if (parameter instanceof Label label) {
          labels.put(label.key(), label.value());
        }
      }
    }

    var mdc = logRecord.getMdcCopy();
    var ndc = logRecord.getNdc();

    var sourceLocation =
        new GoogleCloudLogEntry.SourceLocation(
            logRecord.getSourceFileName(),
            String.valueOf(logRecord.getSourceLineNumber()),
            String.format(
                "%s.%s", logRecord.getSourceClassName(), logRecord.getSourceMethodName()));

    var entry =
        new GoogleCloudLogEntry(
            message,
            severityOf(logRecord.getLevel()),
            new GoogleCloudLogEntry.Timestamp(logRecord.getInstant()),
            null,
            null,
            sourceLocation,
            labels.isEmpty() ? null : labels,
            parameters.isEmpty() ? null : parameters,
            mdc.isEmpty() ? null : mdc,
            ndc.isEmpty() ? null : ndc,
            logRecord.getLevel().intValue() >= 1000 ? ERROR_EVENT_TYPE : null);

    try {
      return jsonb.toJson(entry) + "\n";
    } catch (JsonbException e) {
      e.printStackTrace();
      return message + "\n";
    }
  }

  /**
   * Formats the log message corresponding to {@code logRecord} including a stack trace of the
   * {@link ExtLogRecord#getThrown()} exception if any.
   */
  private String formatMessageWithStackTrace(ExtLogRecord logRecord) {
    var messageStringWriter = new StringWriter();
    var messagePrintWriter = new PrintWriter(messageStringWriter);
    messagePrintWriter.append(this.formatMessage(logRecord));

    if (logRecord.getThrown() != null) {
      messagePrintWriter.println();
      logRecord.getThrown().printStackTrace(messagePrintWriter);
    }

    messagePrintWriter.close();
    return messageStringWriter.toString();
  }

  /** Computes the Google Cloud Logging severity corresponding to a given {@link Level}. */
  private static String severityOf(Level level) {
    if (level.intValue() < 500) {
      return TRACE_LEVEL;
    } else if (level.intValue() < 700) {
      return DEBUG_LEVEL;
    } else if (level.intValue() < 900) {
      return INFO_LEVEL;
    } else if (level.intValue() < 1000) {
      return WARNING_LEVEL;
    } else {
      return ERROR_LEVEL;
    }
  }
}
