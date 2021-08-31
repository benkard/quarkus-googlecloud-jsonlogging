package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.util.Collection;

/**
 * A user-supplied provider for {@link Label}s.
 *
 * <p>Any CDI beans registered under this class are applied to each log entry that is logged.
 *
 * @see StructuredParameterProvider
 */
public interface LabelProvider {

  /** Provides a collection of {@link Label}s to add to each log entry that is logged. */
  Collection<Label> getLabels();
}
