// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.logmanager;

import eu.mulk.quarkus.googlecloud.jsonlogging.Formatter;
import java.util.logging.Handler;
import org.jboss.logmanager.EmbeddedConfigurator;
import org.jboss.logmanager.handlers.ConsoleHandler;

/**
 * A convenient {@link EmbeddedConfigurator} for JBoss Log Manager Embedded (1.1.x and earlier).
 *
 * <p>You can register this class through the {@link java.util.ServiceLoader} mechanism as a
 * provider of the {@link EmbeddedConfigurator} interface (under the name of {@code
 * org.jboss.logmanager.EmbeddedConfigurator}) to automatically register a {@link ConsoleHandler}
 * using {@link Formatter} as the default log output method for the application.
 */
public final class DefaultEmbeddedConfigurator implements EmbeddedConfigurator {

  private final Handler[] rootHandlers;

  /**
   * Constructs a JBoss Log Manager configuration that uses {@link Formatter} and {@link
   * ConsoleHandler} for log output.
   */
  @SuppressWarnings({"java:S2095", "resource"})
  public DefaultEmbeddedConfigurator() {
    rootHandlers = new Handler[] {createConsoleHandler()};
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
  public Handler[] getHandlersOf(String loggerName) {
    if (loggerName.isEmpty()) {
      return rootHandlers;
    } else {
      return EmbeddedConfigurator.NO_HANDLERS;
    }
  }
}
