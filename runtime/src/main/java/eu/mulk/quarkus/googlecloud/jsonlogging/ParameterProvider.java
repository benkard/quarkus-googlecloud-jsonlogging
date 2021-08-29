package eu.mulk.quarkus.googlecloud.jsonlogging;

/**
 * A user-supplied provider for {@link StructuredParameter}s.
 *
 * <p>Any CDI beans registered under this class are applied to each log entry that is logged.
 */
public interface ParameterProvider {

  /** Provides a {@link StructuredParameter} to add to each log entry that is logged. */
  StructuredParameter get();
}
