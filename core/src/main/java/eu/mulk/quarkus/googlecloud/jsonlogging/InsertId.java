// SPDX-FileCopyrightText: © 2024 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.util.Objects;

/**
 * A unique identifier for a log entry.
 *
 * <p>Prevents the duplicate insertion of log entries.  Also serves as a discriminator to order log entries that carry
 * the same time stamp.
 *
 * <p>Will be generated by Google Cloud Logging if not provided.
 *
 * <p>Instances of {@link InsertId} can be passed as log parameters to the {@code *f} family of logging
 * functions on {@link org.jboss.logging.Logger}.
 *
 * <p><strong>Example:</strong>
 *
 * {@snippet :
 * logger.logf("Request rejected: unauthorized.", InsertId.of("123"));
 * }
 *
 * <p>Result:
 *
 * {@snippet lang="json" :
 * {
 *   "textPayload": "Request rejected: unauthorized.",
 *   "logging.googleapis.com/insertId": "123"
 * }
 * }
 *
 * @see Label
 * @see StructuredParameter
 */
public final class InsertId {

  private final String value;

  private InsertId(String value) {
    this.value = value;
  }

  /**
   * Constructs an {@link InsertId} from a string.
   *
   * @param value the value of the insertion ID.
   * @return the newly constructed {@link InsertId}, ready to be passed to a logging function.
   */
  public static InsertId of(String value) {
    return new InsertId(value);
  }

  /**
   * The value of the label.
   *
   * @return the value of the label.
   */
  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (InsertId) obj;
    return Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "InsertId[value=" + value + ']';
  }
}
