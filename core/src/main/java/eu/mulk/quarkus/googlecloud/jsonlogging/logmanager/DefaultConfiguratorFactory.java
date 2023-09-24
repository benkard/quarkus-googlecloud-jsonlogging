// SPDX-FileCopyrightText: © 2023 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.logmanager;

import eu.mulk.quarkus.googlecloud.jsonlogging.Formatter;
import java.io.InputStream;
import java.util.logging.Handler;
import org.jboss.logmanager.ConfiguratorFactory;
import org.jboss.logmanager.LogContext;
import org.jboss.logmanager.LogContextConfigurator;
import org.jboss.logmanager.handlers.ConsoleHandler;

/**
 * A convenient {@link ConfiguratorFactory} for JBoss Log Manager.
 *
 * <p>You can register this class through the {@link java.util.ServiceLoader} mechanism as a
 * provider of the {@link ConfiguratorFactory} interface (under the name of {@code
 * org.jboss.logmanager.ConfiguratorFactory}) to automatically register a {@link ConsoleHandler}
 * using {@link Formatter} as the default log output method for the application.
 */
public final class DefaultConfiguratorFactory implements ConfiguratorFactory {

  private final Handler[] rootHandlers;
  private final ConfiguratorFactory upstreamConfiguratorFactory;

  /**
   * Constructs a JBoss Log Manager configuration that uses {@link Formatter} and {@link
   * ConsoleHandler} for log output.
   */
  @SuppressWarnings({"java:S2095", "resource"})
  public DefaultConfiguratorFactory() {
    rootHandlers = new Handler[] {createConsoleHandler()};
    upstreamConfiguratorFactory =
        new org.jboss.logmanager.configuration.DefaultConfiguratorFactory();
  }

  /**
   * Creates a {@link ConsoleHandler} that uses {@link Formatter} for formatting.
   *
   * @return a preconfigured {@link ConsoleHandler}.
   */
  public static ConsoleHandler createConsoleHandler() {
    return new DefaultConsoleHandler();
  }

  @Override
  public LogContextConfigurator create() {
    var upstreamConfigurator = upstreamConfiguratorFactory.create();
    return new LogContextConfigurator() {
      @Override
      public void configure(LogContext logContext, InputStream inputStream) {
        upstreamConfigurator.configure(logContext, inputStream);
        var logger = logContext.getLogger("");
        logger.setHandlers(rootHandlers);
      }
    };
  }

  @Override
  public int priority() {
    return 50;
  }
}
