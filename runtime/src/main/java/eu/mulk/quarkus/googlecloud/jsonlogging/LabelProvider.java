package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.util.Collection;

/**
 * A user-supplied provider for {@link Label}s.
 *
 * <p>Any CDI beans registered under this class are applied to each log entry that is logged.
 *
 * <p><strong>Example:</strong>
 *
 * <pre>{@code
 * @Singleton
 * @Unremovable
 * public final class RequestIdLabelProvider implements LabelProvider {
 *
 *   @Override
 *   public Collection<Label> getLabels() {
 *     return List.of(Label.of("requestId", RequestContext.current().getRequestId()));
 *   }
 * }
 * }</pre>
 *
 * Result:
 *
 * <pre>{@code
 * {
 *   "textPayload": "Request rejected: unauthorized.",
 *   "labels": {
 *     "requestId": "123"
 *   }
 * }
 * }</pre>
 *
 * @see StructuredParameterProvider
 */
public interface LabelProvider {

  /**
   * Provides a collection of {@link Label}s to add to each log entry that is logged.
   *
   * @return a collection of {@link Label}s to add to each log entry that is logged.
   */
  Collection<Label> getLabels();
}
