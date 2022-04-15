// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.util.Objects;

/**
 * A label usable to tag a log message.
 *
 * <p>Instances of {@link Label} can be passed as log parameters to the {@code *f} family of logging
 * functions on {@link org.jboss.logging.Logger}.
 *
 * <p><strong>Example:</strong>
 *
 * {@snippet :
 * logger.logf("Request rejected: unauthorized.", Label.of("requestId", "123"));
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
 * @see KeyValueParameter
 * @see StructuredParameter
 */
public final class Label {

  private final String key;
  private final String value;

  private Label(String key, String value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Constructs a {@link Label} from a key (i.e. name) and a value.
   *
   * <p>It is often useful for the key to be a {@link String} constant that is shared by multiple
   * parts of the program.
   *
   * @param key the key (name) of the label.
   * @param value the value of the label.
   * @return the newly constructed {@link Label}, ready to be passed to a logging function.
   */
  public static Label of(String key, String value) {
    return new Label(key, value);
  }

  /**
   * The name of the label.
   *
   * <p>It is often useful for this to be a {@link String} constant that is shared by multiple parts
   * of the program.
   *
   * @return the name of the label.
   */
  public String key() {
    return key;
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
    var that = (Label) obj;
    return Objects.equals(this.key, that.key) && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    return "Label[" + "key=" + key + ", " + "value=" + value + ']';
  }
}
