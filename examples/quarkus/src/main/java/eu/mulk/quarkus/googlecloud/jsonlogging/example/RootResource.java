// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: GPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.example;

import eu.mulk.quarkus.googlecloud.jsonlogging.KeyValueParameter;
import eu.mulk.quarkus.googlecloud.jsonlogging.Label;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    // return "ok";
  }
}
