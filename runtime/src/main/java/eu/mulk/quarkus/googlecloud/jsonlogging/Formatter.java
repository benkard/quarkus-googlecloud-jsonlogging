package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

/**
 * Formats log records as JSON for consumption by Google Cloud Logging.
 *
 * <p>Meant to be used in containers running on Google Kubernetes Engine (GKE).
 *
 * @see LogEntry
 */
class Formatter extends ExtFormatter {

  private static final String TRACE_LEVEL = "TRACE";
  private static final String DEBUG_LEVEL = "DEBUG";
  private static final String INFO_LEVEL = "INFO";
  private static final String WARNING_LEVEL = "WARNING";
  private static final String ERROR_LEVEL = "ERROR";

  private static final String ERROR_EVENT_TYPE =
      "type.googleapis.com/google.devtools.clouderrorreporting.v1beta1.ReportedErrorEvent";

  @Override
  public String format(ExtLogRecord logRecord) {
    var message = formatMessageWithStackTrace(logRecord);

    List<StructuredParameter> parameters = new ArrayList<>();
    Map<String, String> labels = new HashMap<>();
    if (logRecord.getParameters() != null) {
      for (var parameter : logRecord.getParameters()) {
        if (parameter instanceof StructuredParameter sparam) {
          parameters.add(sparam);
        } else if (parameter instanceof Label label) {
          labels.put(label.key(), label.value());
        }
      }
    }

    var mdc = logRecord.getMdcCopy();
    var ndc = logRecord.getNdc();

    var sourceLocation =
        new LogEntry.SourceLocation(
            logRecord.getSourceFileName(),
            String.valueOf(logRecord.getSourceLineNumber()),
            String.format(
                "%s.%s", logRecord.getSourceClassName(), logRecord.getSourceMethodName()));

    var entry =
        new LogEntry(
            message,
            severityOf(logRecord.getLevel()),
            new LogEntry.Timestamp(logRecord.getInstant()),
            null,
            null,
            sourceLocation,
            labels,
            parameters,
            mdc,
            ndc,
            logRecord.getLevel().intValue() >= 1000 ? ERROR_EVENT_TYPE : null);

    return entry.json().build().toString() + "\n";
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
