package eu.mulk.quarkus.googlecloud.jsonlogging.example;

import eu.mulk.quarkus.googlecloud.jsonlogging.KeyValueParameter;
import eu.mulk.quarkus.googlecloud.jsonlogging.Label;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

@Produces("text/plain")
@Path("/")
@ApplicationScoped
public class RootResource {

  static final Logger log = Logger.getLogger(RootResource.class);

  @PostConstruct
  public void init() {
    log.warn("Hey!");
  }

  @GET
  public String hello() {
    MDC.put("requestMethod", "GET");
    log.infof(
        "Hello %s.",
        "Mulkiatsch",
        KeyValueParameter.of("a", "b"),
        Label.of("app", "foo"),
        KeyValueParameter.of("version", 10));
    throw new IllegalStateException();
  }
}
