import eu.mulk.quarkus.googlecloud.jsonlogging.Formatter;

module quarkus.googlecloud.jsonlogging {
  requires java.logging;
  requires java.json;
  requires jboss.logmanager.embedded;
  requires quarkus.core;
  requires smallrye.common.constraint;

  exports eu.mulk.quarkus.googlecloud.jsonlogging;

  provides java.util.logging.Formatter with
      Formatter;
  provides org.jboss.logmanager.ExtFormatter with
      Formatter;
}
