package eu.mulk.quarkus.googlecloud.jsonlogging;

import jakarta.json.Json;
import org.jboss.logmanager.ExtLogRecord;
import org.jboss.logmanager.Level;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class FormatterTest {

  @Test
  void simpleRecord() {
    var logRecord = new ExtLogRecord(Level.INFO, "Hello, world!", FormatterTest.class.getName());

    var formatter = new Formatter(List.of(), List.of());
    var formattingResult = formatter.format(logRecord);

    assertLinesMatch(
        List.of(
            "\\{"
                + "\"message\":\"Hello, world!\","
                + "\"severity\":\"INFO\","
                + "\"timestamp\":\\{\"seconds\":\\d+,\"nanos\":\\d+\\},"
                + "\"logging.googleapis.com/sourceLocation\":"
                + "\\{\"file\":\"ReflectionUtils.java\","
                + "\"line\":\"\\d+\","
                + "\"function\":\"org.junit.platform.commons.util.ReflectionUtils.invokeMethod\""
                + "\\}"
                + "\\}\n"),
        List.of(formattingResult));
  }

  @Test
  void structuredRecord() {
    var parameterProvider =
        new StructuredParameterProvider() {
          @Override
          public StructuredParameter getParameter() {
            var b = Json.createObjectBuilder();
            b.add("traceId", "39f9a49a9567a8bd7087b708f8932550");
            b.add("spanId", "c7431b14630b633d");
            return () -> b;
          }
        };

    var labelProvider =
        new LabelProvider() {
          @Override
          public Collection<Label> getLabels() {
            return List.of(Label.of("requestId", "123"));
          }
        };

    var logRecord = new ExtLogRecord(Level.INFO, "Hello, world!", FormatterTest.class.getName());
    logRecord.setParameters(
        new Object[] {
          (StructuredParameter)
              () -> Json.createObjectBuilder().add("one", 1).add("two", 2.0).add("yes", true),
          Label.of("a", "b")
        });

    var formatter = new Formatter(List.of(parameterProvider), List.of(labelProvider));
    var formattingResult = formatter.format(logRecord);
    assertLinesMatch(
        List.of(
            "\\{"
                + "\"logging.googleapis.com/labels\":\\{\"a\":\"b\",\"requestId\":\"123\"\\},"
                + "\"message\":\"Hello, world!\","
                + "\"severity\":\"INFO\","
                + "\"timestamp\":\\{\"seconds\":\\d+,\"nanos\":\\d+\\},"
                + "\"logging.googleapis.com/sourceLocation\":"
                + "\\{\"file\":\"ReflectionUtils.java\","
                + "\"line\":\"\\d+\","
                + "\"function\":\"org.junit.platform.commons.util.ReflectionUtils.invokeMethod\""
                + "\\},"
                + "\"traceId\":\"39f9a49a9567a8bd7087b708f8932550\","
                + "\"spanId\":\"c7431b14630b633d\","
                + "\"one\":1,"
                + "\"two\":2.0,"
                + "\"yes\":true"
                + "\\}\n"),
        List.of(formattingResult));
  }
}
