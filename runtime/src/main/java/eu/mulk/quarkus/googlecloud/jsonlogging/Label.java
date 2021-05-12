package eu.mulk.quarkus.googlecloud.jsonlogging;

import java.util.Objects;

public final class Label {

  private final String key;
  private final String value;

  private Label(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public static Label of(String key, String value) {
    return new Label(key, value);
  }

  public String key() {
    return key;
  }

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
