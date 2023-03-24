// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import jakarta.json.JsonObjectBuilder;

/**
 * A structured parameter usable as logging payload.
 *
 * <p>Any instance of {@link StructuredParameter} can be passed as a log parameter to the {@code *f}
 * family of logging functions on {@link org.jboss.logging.Logger}.
 *
 * <p>Example:
 *
 * {@snippet :
 * StructuredParameter p1 = ...;
 * StructuredParameter p2 = ...;
 *
 * logger.logf("Something interesting happened.", p1, p2);
 * }
 *
 * @see KeyValueParameter
 * @see Label
 */
public interface StructuredParameter {

  /**
   * The JSON to be embedded in the payload of the log entry.
   *
   * <p>May contain multiple keys and values as well as nested objects. Each top-level entry of the
   * returned object is embedded as a top-level entry in the payload of the log entry.
   *
   * @return A {@link JsonObjectBuilder} holding a set of key–value pairs.
   */
  JsonObjectBuilder json();
}
