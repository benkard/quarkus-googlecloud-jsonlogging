// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.util.Collection;

/**
 * A user-supplied provider for {@link Label}s.
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
 * public final class RequestIdLabelProvider implements LabelProvider {
 *
 *   @Override
 *   public Collection<Label> getLabels() {
 *     return List.of(Label.of("requestId", RequestContext.current().getRequestId()));
 *   }
 * }
 * }
 *
 * <p>Result:
 *
 * {@snippet lang="json" :
 * {
 *   "textPayload": "Request rejected: unauthorized.",
 *   "labels": {
 *     "requestId": "123"
 *   }
 * }
 * }
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
