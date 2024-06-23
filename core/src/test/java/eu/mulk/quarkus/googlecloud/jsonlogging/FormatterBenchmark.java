package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.util.List;
import org.jboss.logmanager.ExtLogRecord;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(value = 1)
@State(org.openjdk.jmh.annotations.Scope.Benchmark)
public class FormatterBenchmark {

  private ExtLogRecord simpleLogRecord;
  private ExtLogRecord structuredLogRecord;
  private ExtLogRecord massivelyStructuredLogRecord;
  private Formatter formatter;

  @Setup
  public void setup() {
    simpleLogRecord = FormatterTest.makeSimpleRecord();
    structuredLogRecord = FormatterTest.makeStructuredRecord();
    massivelyStructuredLogRecord = FormatterTest.makeMassivelyStructuredRecord();
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
}
