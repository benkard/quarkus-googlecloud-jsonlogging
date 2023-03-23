// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

/**
 * A simple single key–value pair forming a {@link StructuredParameter}.
 *
 * <p>This class is suitable for the common case of logging a key–value pair as parameter to the
 * {@code *f} family of logging functions on {@link org.jboss.logging.Logger}. For advanced use
 * cases, provide your own implementation of {@link StructuredParameter}.
 *
 * <p><strong>Example:</strong>
 *
 * {@snippet :
 * logger.infof("Application starting.", StructuredParameter.of("version", "1.0"));
 * }
 *
 * <p>Result:
 *
 * {@snippet lang="json" :
 * {
 *   "jsonPayload": {
 *     "message": "Application starting.",
 *     "version": "1.0"
 *   }
 * }
 * }
 *
 * @see Label
 * @see StructuredParameter
 */
public final class KeyValueParameter implements StructuredParameter {

  private final String key;
  private final JsonValue value;

  private KeyValueParameter(String key, JsonValue value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Creates a {@link KeyValueParameter} from a {@link String} value.
   *
   * <p>The resulting JSON value is of type {@code string}.
   *
   * @param key the key part of the key–value pair.
   * @param value the value part of the key–value pair.
   * @return the newly constructed parameter, ready to be passed to a logging function.
   */
  public static KeyValueParameter of(String key, String value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  /**
   * Creates a {@link KeyValueParameter} from an {@code int} value.
   *
   * <p>The resulting JSON value is of type {@code number}.
   *
   * @param key the key part of the key–value pair.
   * @param value the value part of the key–value pair.
   * @return the newly constructed parameter, ready to be passed to a logging function.
   */
  public static KeyValueParameter of(String key, int value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  /**
   * Creates a {@link KeyValueParameter} from a {@code long} value.
   *
   * <p>The resulting JSON value is of type {@code number}.
   *
   * @param key the key part of the key–value pair.
   * @param value the value part of the key–value pair.
   * @return the newly constructed parameter, ready to be passed to a logging function.
   */
  public static KeyValueParameter of(String key, long value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  /**
   * Creates a {@link KeyValueParameter} from a {@code double} value.
   *
   * <p>The resulting JSON value is of type {@code number}.
   *
   * @param key the key part of the key–value pair.
   * @param value the value part of the key–value pair.
   * @return the newly constructed parameter, ready to be passed to a logging function.
   */
  public static KeyValueParameter of(String key, double value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  /**
   * Creates a {@link KeyValueParameter} from a {@link BigDecimal} value.
   *
   * <p>The resulting JSON value is of type {@code number}.
   *
   * @param key the key part of the key–value pair.
   * @param value the value part of the key–value pair.
   * @return the newly constructed parameter, ready to be passed to a logging function.
   */
  public static KeyValueParameter of(String key, BigDecimal value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  /**
   * Creates a {@link KeyValueParameter} from a {@link BigInteger} value.
   *
   * <p>The resulting JSON value is of type {@code number}.
   *
   * @param key the key part of the key–value pair.
   * @param value the value part of the key–value pair.
   * @return the newly constructed parameter, ready to be passed to a logging function.
   */
  public static KeyValueParameter of(String key, BigInteger value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  /**
   * Creates a {@link KeyValueParameter} from a {@code boolean} value.
   *
   * <p>The resulting JSON value is of type {@code boolean}.
   *
   * @param key the key part of the key–value pair.
   * @param value the value part of the key–value pair.
   * @return the newly constructed parameter, ready to be passed to a logging function.
   */
  public static KeyValueParameter of(String key, boolean value) {
    return new KeyValueParameter(key, value ? JsonValue.TRUE : JsonValue.FALSE);
  }

  @Override
  public JsonObjectBuilder json() {
    return Json.createObjectBuilder().add(key, value);
  }

  /**
   * The key part of the key–value pair.
   *
   * @return the key part of the key–value pair.
   */
  public String key() {
    return key;
  }

  /**
   * The value part of the key–value pair.
   *
   * <p>Can be of any non-composite JSON type (i.e. {@code string}, {@code number}, or {@code
   * boolean}).
   *
   * @return the value pairt of the key–value pair.
   */
  public JsonValue value() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (KeyValueParameter) obj;
    return Objects.equals(this.key, that.key) && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    return "KeyValueParameter[" + "key=" + key + ", " + "value=" + value + ']';
  }
}
