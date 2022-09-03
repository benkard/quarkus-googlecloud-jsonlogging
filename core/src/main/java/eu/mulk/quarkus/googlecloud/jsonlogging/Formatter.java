// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

/**
 * Formats log records as JSON for consumption by Google Cloud Logging.
 *
 * <p>Meant to be used in containers running on Google Kubernetes Engine (GKE).
 *
 * @see LogEntry
 */
public class Formatter extends ExtFormatter {

  private static final String TRACE_LEVEL = "TRACE";
  private static final String DEBUG_LEVEL = "DEBUG";
  private static final String INFO_LEVEL = "INFO";
  private static final String WARNING_LEVEL = "WARNING";
  private static final String ERROR_LEVEL = "ERROR";

  private static final String ERROR_EVENT_TYPE =
      "type.googleapis.com/google.devtools.clouderrorreporting.v1beta1.ReportedErrorEvent";

  private final List<StructuredParameterProvider> parameterProviders;
  private final List<LabelProvider> labelProviders;

  /**
   * Constructs a {@link Formatter} with custom configuration.
   *
   * <p><strong>Note:</strong> This constructor does not automatically discover providers using the
   * {@link ServiceLoader} mechanism. See {@link #load} for this case use.
   *
   * @param parameterProviders the {@link StructuredParameterProvider}s to apply to each log entry.
   * @param labelProviders the {@link LabelProvider}s to apply to each log entry.
   */
  public Formatter(
      Collection<StructuredParameterProvider> parameterProviders,
      Collection<LabelProvider> labelProviders) {
    this.parameterProviders = List.copyOf(parameterProviders);
    this.labelProviders = List.copyOf(labelProviders);
  }

  /**
   * Constructs a {@link Formatter} with parameter and label providers supplied by {@link
   * ServiceLoader}.
   *
   * <p>In addition to the providers supplied as parameters, this factory method loads all {@link
   * StructuredParameterProvider}s and {@link LabelProvider}s found through the {@link
   * ServiceLoader} mechanism.
   *
   * @param parameterProviders the {@link StructuredParameterProvider}s to apply to each log entry.
   * @param labelProviders the {@link LabelProvider}s to apply to each log entry.
   * @return a new formatter.
   */
  public static Formatter load(
      Collection<StructuredParameterProvider> parameterProviders,
      Collection<LabelProvider> labelProviders) {
    parameterProviders = new ArrayList<>(parameterProviders);
    parameterProviders.addAll(loadStructuredParameterProviders());

    labelProviders = new ArrayList<>(labelProviders);
    labelProviders.addAll(loadLabelProviders());

    return new Formatter(parameterProviders, labelProviders);
  }

  private static List<StructuredParameterProvider> loadStructuredParameterProviders() {
    return ServiceLoader.load(StructuredParameterProvider.class, Formatter.class.getClassLoader())
        .stream()
        .map(Provider::get)
        .collect(Collectors.toList());
  }

  private static List<LabelProvider> loadLabelProviders() {
    return ServiceLoader.load(LabelProvider.class, Formatter.class.getClassLoader()).stream()
        .map(Provider::get)
        .collect(Collectors.toList());
  }

  @Override
  public String format(ExtLogRecord logRecord) {
    var message = formatMessageWithStackTrace(logRecord);

    List<StructuredParameter> parameters = new ArrayList<>();
    Map<String, String> labels = new HashMap<>();

    var providerContext = new ProviderContext(logRecord);

    for (var parameterProvider : parameterProviders) {
      var parameter = parameterProvider.getParameter(providerContext);
      if (parameter != null) {
        parameters.add(parameter);
      }
    }

    for (var labelProvider : labelProviders) {
      var providedLabels = labelProvider.getLabels(providerContext);
      if (providedLabels != null) {
        for (var label : providedLabels) {
          labels.put(label.key(), label.value());
        }
      }
    }

    if (logRecord.getParameters() != null) {
      for (var parameter : logRecord.getParameters()) {
        if (parameter instanceof StructuredParameter) {
          parameters.add((StructuredParameter) parameter);
        } else if (parameter instanceof Label) {
          var label = (Label) parameter;
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

  /**
   * An implementation of {@link LabelProvider.Context} and {@link
   * StructuredParameterProvider.Context}.
   */
  private static class ProviderContext
      implements LabelProvider.Context, StructuredParameterProvider.Context {

    private final String loggerName;
    private final long sequenceNumber;
    private final String threadName;

    private ProviderContext(ExtLogRecord logRecord) {
      loggerName = logRecord.getLoggerName();
      sequenceNumber = logRecord.getSequenceNumber();
      threadName = logRecord.getThreadName();
    }

    @Override
    public String loggerName() {
      return loggerName;
    }

    @Override
    public long sequenceNumber() {
      return sequenceNumber;
    }

    @Override
    public String threadName() {
      return threadName;
    }
  }
}
