// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.logmanager;

import eu.mulk.quarkus.googlecloud.jsonlogging.Formatter;
import java.io.InputStream;
import java.util.Collections;
import org.jboss.logmanager.handlers.ConsoleHandler;

/**
 * A {@link ConsoleHandler} preconfigured with {@link Formatter}.
 *
 * <p>Useful as a handler for {@link java.util.logging}.
 *
 * <p>If you have a {@code logging.properties} file (see {@link
 * java.util.logging.LogManager#readConfiguration(InputStream)}), you can use this handler by
 * setting the following properties:
 *
 * {@snippet lang="properties" :
 * handlers = eu.mulk.quarkus.googlecloud.jsonlogging.logmanager.DefaultConsoleHandler
 * }
 *
 * <p><strong>Note:</strong> You can use {@code org.slf4j.bridge.SLF4JBridgeHandler} from {@code
 * org.slf4j:jul-to-slf4j} instead if you also have {@code org.jboss.slf4j:slf4j-jboss-logmanager}
 * on the class path. In comparison to this class, which relies on the relatively efficient {@link
 * org.jboss.logmanager.ExtLogRecord#wrap}, routing through SLF4J incurs additional overhead because
 * of the necessary conversions between SLF4J's log entry structure and {@link
 * java.util.logging.LogRecord}.
 *
 * <h2>Usage with Spring Boot</h2>
 *
 * <p>In case you are using Spring Boot, note that in addition to ensuring that {@code
 * org.springframework.boot.logging.java.JavaLoggingSystem} is the logging system in use (see
 * below), you need to accompany this with an entry in {@code application.properties} that points to
 * your {@code logging.properties} file:
 *
 * {@snippet lang="properties" :
 * logging.config = classpath:logging.properties
 * }
 *
 * <p>In order to ensure that Spring Boot chooses {@code JavaLoggingSystem} over other
 * implementations, make sure that no other logging backends are present on the class path. A simple
 * way of doing this is by relying on {@code spring-boot-starter-logging} while excluding Logback:
 *
 * {@snippet lang="xml" :
 * <dependency>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-starter</artifactId>
 *   <exclusions>
 *     <exclusion>
 *       <groupId>ch.qos.logback</groupId>
 *       <artifactId>logback-classic</artifactId>
 *     </exclusion>
 *   </exclusions>
 * </dependency>
 * }
 *
 * <p>You will probably want to include at least {@code org.jboss.slf4j:slf4j-jboss-logmanager} as
 * well. In addition, {@code org.slf4j:jcl-over-slf4j}, {@code
 * org.jboss.logmanager:log4j-jboss-logmanager}, and {@code
 * org.jboss.logmanager:log4j2-jboss-logmanager} may be useful, but are not required.
 */
@SuppressWarnings("java:S110")
public final class DefaultConsoleHandler extends ConsoleHandler {

  /** Constructs console handler with a formatter created by {@link Formatter#load}. */
  public DefaultConsoleHandler() {
    super(Formatter.load(Collections.emptyList(), Collections.emptyList()));
  }
}
