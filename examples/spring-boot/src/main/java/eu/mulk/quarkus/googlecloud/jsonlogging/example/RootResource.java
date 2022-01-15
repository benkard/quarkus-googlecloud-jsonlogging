package eu.mulk.quarkus.googlecloud.jsonlogging.example;

import eu.mulk.quarkus.googlecloud.jsonlogging.KeyValueParameter;
import eu.mulk.quarkus.googlecloud.jsonlogging.Label;
import javax.annotation.PostConstruct;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class RootResource {

  static final Logger log = Logger.getLogger(RootResource.class);

  @PostConstruct
  public void init() {
    log.warn("Hey!");
  }

  @GetMapping(produces = "text/plain")
  public String hello() {
    MDC.put("requestMethod", "GET");
    log.infof(
        "Hello %s.",
        "Mulkiatsch",
        KeyValueParameter.of("a", "b"),
        Label.of("app", "foo"),
        KeyValueParameter.of("version", 10));
    throw new IllegalStateException();
    // return "ok";
  }
}
