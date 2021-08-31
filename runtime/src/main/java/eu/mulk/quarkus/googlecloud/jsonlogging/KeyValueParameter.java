package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * A simple single key--value pair forming a {@link StructuredParameter}.
 *
 * <p>This class is suitable for the common case of logging a key—value pair as parameter to the
 * {@code *f} family of logging functions on {@link org.jboss.logging.Logger}. For advanced use
 * cases, provide your own implementation of {@link StructuredParameter}.
 *
 * <p>Example:
 *
 * <pre>{@code
 * logger.infof("Application starting.", StructuredParameter.of("version", "1.0"));
 * }</pre>
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

  public static KeyValueParameter of(String key, String value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  public static KeyValueParameter of(String key, int value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  public static KeyValueParameter of(String key, long value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  public static KeyValueParameter of(String key, double value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  public static KeyValueParameter of(String key, BigDecimal value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  public static KeyValueParameter of(String key, BigInteger value) {
    return new KeyValueParameter(key, Json.createValue(value));
  }

  public static KeyValueParameter of(String key, boolean value) {
    return new KeyValueParameter(key, value ? JsonValue.TRUE : JsonValue.FALSE);
  }

  @Override
  public JsonObjectBuilder json() {
    return Json.createObjectBuilder().add(key, value);
  }

  public String key() {
    return key;
  }

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
