package eu.mulk.quarkus.googlecloud.jsonlogging;

import javax.json.JsonObjectBuilder;

/**
 * A structured parameter usable as logging payload.
 *
 * <p>Any instance of {@link StructuredParameter} can be passed as a log parameter to the {@code *f}
 * family of logging functions on {@link org.jboss.logging.Logger}.
 *
 * <p>Example:
 *
 * <pre>{@code
 * StructuredParameter p1 = ...;
 * StructuredParameter p2 = ...;
 *
 * logger.logf("Something interesting happened.", p1, p2);
 * }</pre>
 *
 * @see KeyValueParameter
 * @see Label
 */
public interface StructuredParameter {

  /**
   * The JSON to be embedded in the log entry.
   *
   * <p>May contain multiple keys and values as well as nested objects. Each top-level entry of the
   * returned object is embedded as a top-level entry in the log entry.
   */
  JsonObjectBuilder json();
}
