package eu.mulk.quarkus.googlecloud.jsonlogging;

public record Label(String key, String value) {

  public static Label of(String key, String value) {
    return new Label(key, value);
  }
}
