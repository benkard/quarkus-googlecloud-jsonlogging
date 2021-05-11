module quarkus.googlecloud.jsonlogging {
  requires java.logging;
  requires java.json;
  requires jboss.logmanager.embedded;
  requires quarkus.core;
  requires smallrye.common.constraint;

  exports eu.mulk.quarkus.googlecloud.jsonlogging;
}
