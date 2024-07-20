// SPDX-FileCopyrightText: © 2024 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import static java.util.logging.Level.FINEST;

import java.util.List;
import java.util.logging.LogRecord;
import org.jboss.logmanager.formatters.Formatters;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(value = 1)
@State(org.openjdk.jmh.annotations.Scope.Benchmark)
public class FormatterBenchmark {

  private static final LogRecord NULL_LOG_RECORD = new LogRecord(FINEST, "");
  private static final java.util.logging.Formatter NULL_FORMATTER = Formatters.nullFormatter();

  private LogRecord simpleLogRecord = NULL_LOG_RECORD;
  private LogRecord structuredLogRecord = NULL_LOG_RECORD;
  private LogRecord massivelyStructuredLogRecord = NULL_LOG_RECORD;
  private LogRecord nestedLogRecord = NULL_LOG_RECORD;
  private java.util.logging.Formatter formatter = NULL_FORMATTER;

  @Setup
  public void setup() {
    simpleLogRecord = FormatterTest.makeSimpleRecord();
    structuredLogRecord = FormatterTest.makeStructuredRecord();
    massivelyStructuredLogRecord = FormatterTest.makeMassivelyStructuredRecord();
    nestedLogRecord = FormatterTest.makeNestedRecord();
    formatter = new Formatter(List.of(), List.of());
  }

  @Benchmark
  public void simpleLogRecord(Blackhole blackhole) {
    blackhole.consume(formatter.format(simpleLogRecord));
  }

  @Benchmark
  public void structuredLogRecord(Blackhole blackhole) {
    blackhole.consume(formatter.format(structuredLogRecord));
  }

  @Benchmark
  public void massivelyStructuredLogRecord(Blackhole blackhole) {
    var f = formatter.format(massivelyStructuredLogRecord);
    blackhole.consume(f);
  }

  @Benchmark
  public void nestedLogRecord(Blackhole blackhole) {
    var f = formatter.format(nestedLogRecord);
    blackhole.consume(f);
  }
}
