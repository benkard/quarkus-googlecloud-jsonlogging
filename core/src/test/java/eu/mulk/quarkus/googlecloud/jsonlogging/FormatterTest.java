package eu.mulk.quarkus.googlecloud.jsonlogging;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import jakarta.json.Json;
import java.util.Collection;
import java.util.List;
import org.jboss.logmanager.ExtLogRecord;
import org.jboss.logmanager.Level;
import org.junit.jupiter.api.Test;

class FormatterTest {

  @Test
  void simpleRecord() {
    var logRecord = makeSimpleRecord();

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

  static ExtLogRecord makeSimpleRecord() {
    return new ExtLogRecord(Level.INFO, "Hello, world!", FormatterTest.class.getName());
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

    var logRecord = makeStructuredRecord();

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

  static ExtLogRecord makeStructuredRecord() {
    var logRecord = makeSimpleRecord();
    logRecord.setParameters(
        new Object[] {
          (StructuredParameter)
              () -> Json.createObjectBuilder().add("one", 1).add("two", 2.0).add("yes", true),
          Label.of("a", "b")
        });
    return logRecord;
  }

  @Test
  void massivelyStructuredRecord() {
    var logRecord = makeMassivelyStructuredRecord();

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
                + "\\},"
                + "\"int-0\":0,\"int-1\":1,\"int-2\":2,\"int-3\":3,\"int-4\":4,\"int-5\":5,\"int-6\":6,\"int-7\":7,\"int-8\":8,\"int-9\":9,"
                + "\"double-10\":10.0,\"double-11\":11.0,\"double-12\":12.0,\"double-13\":13.0,\"double-14\":14.0,"
                + "\"double-15\":15.0,\"double-16\":16.0,\"double-17\":17.0,\"double-18\":18.0,\"double-19\":19.0,"
                + "\"boolean-20\":true,\"boolean-21\":false,\"boolean-22\":true,\"boolean-23\":false,\"boolean-24\":true,"
                + "\"boolean-25\":false,\"boolean-26\":true,\"boolean-27\":false,\"boolean-28\":true,\"boolean-29\":false,"
                + "\"string-30\":\"30\",\"string-31\":\"31\",\"string-32\":\"32\",\"string-33\":\"33\",\"string-34\":\"34\","
                + "\"string-35\":\"35\",\"string-36\":\"36\",\"string-37\":\"37\",\"string-38\":\"38\",\"string-39\":\"39\""
                + "\\}\n"),
        List.of(formattingResult));
  }

  static ExtLogRecord makeMassivelyStructuredRecord() {
    var logRecord = FormatterTest.makeSimpleRecord();
    logRecord.setParameters(
        new Object[] {
          (StructuredParameter)
              () -> {
                var b = Json.createObjectBuilder();
                for (int i = 0; i < 10; i++) {
                  b.add("int-" + i, i);
                }
                for (int i = 10; i < 20; i++) {
                  b.add("double-" + i, (double) i);
                }
                for (int i = 20; i < 30; i++) {
                  b.add("boolean-" + i, i % 2 == 0);
                }
                for (int i = 30; i < 40; i++) {
                  b.add("string-" + i, String.valueOf(i));
                }
                return b;
              }
        });
    return logRecord;
  }
}
