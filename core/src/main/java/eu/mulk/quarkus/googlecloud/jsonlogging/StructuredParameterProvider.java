// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

/**
 * A user-supplied provider for {@link StructuredParameter}s.
 *
 * <p>Instances of this interface that are registered with the {@link Formatter} are applied to each
 * log entry that is logged.
 *
 * <p>If you are using the Quarkus extension, any CDI beans registered under this interface are
 * registered automatically.
 *
 * <p><strong>Example:</strong>
 *
 * {@snippet :
 * @Singleton
 * @Unremovable
 * public final class TraceLogParameterProvider implements StructuredParameterProvider {
 *
 *   @Override
 *   public StructuredParameter getParameter() {
 *     var b = Json.createObjectBuilder();
 *     b.add("traceId", Span.current().getSpanContext().getTraceId());
 *     b.add("spanId", Span.current().getSpanContext().getSpanId());
 *     return () -> b;
 *   }
 * }
 * }
 *
 * Result:
 *
 * {@snippet lang="json" :
 * {
 *   "jsonPayload": {
 *     "message": "Request rejected: unauthorized.",
 *     "traceId": "39f9a49a9567a8bd7087b708f8932550",
 *     "spanId": "c7431b14630b633d"
 *   }
 * }
 * }
 *
 * @see LabelProvider
 */
public interface StructuredParameterProvider {

  /**
   * Provides a {@link StructuredParameter} to add to each log entry that is logged.
   *
   * <p>It is often useful to return a custom {@link StructuredParameter} rather than a {@link
   * KeyValueParameter} from this method. This way multiple key–value pairs can be generated by a
   * single invocation.
   *
   * <p>If {@link #getParameter(Context)} is implemented, this method is ignored.
   *
   * @return a {@link StructuredParameter} to add to each log entry that is logged.
   * @see #getParameter(Context)
   */
  default StructuredParameter getParameter() {
    return null;
  }

  /**
   * Provides a {@link StructuredParameter} to add to each log entry that is logged.
   *
   * <p>It is often useful to return a custom {@link StructuredParameter} rather than a {@link
   * KeyValueParameter} from this method. This way multiple key–value pairs can be generated by a
   * single invocation.
   *
   * <p>Delegates to {@link #getParameter()} by default.
   *
   * @return a {@link StructuredParameter} to add to each log entry that is logged.
   */
  default StructuredParameter getParameter(Context context) {
    return getParameter();
  }

  /** Contextual data available to {@link #getParameter(Context)}. */
  interface Context extends ProviderContext {}
}
