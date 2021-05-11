package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public record KeyValueParameter(String key, JsonValue value) implements StructuredParameter {

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
}
