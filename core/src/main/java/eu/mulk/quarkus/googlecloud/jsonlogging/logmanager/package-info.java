// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

/**
 * Integration with JBoss Log Manager and {@link java.util.logging}.
 *
 * <p>Provides classes that can be used to conveniently configure the JBoss Log Manager ({@link
 * org.jboss.logmanager.LogManager}) as well as {@link java.util.logging} to use the Google Cloud
 * JSON Logging formatter ({@link eu.mulk.quarkus.googlecloud.jsonlogging.Formatter}).
 *
 * <p>{@link eu.mulk.quarkus.googlecloud.jsonlogging.logmanager.DefaultEmbeddedConfigurator} can be
 * set as a provided implementation of {@link org.jboss.logmanager.EmbeddedConfigurator} via the
 * standard {@link java.util.ServiceLoader} mechanism.
 *
 * <p>{@link eu.mulk.quarkus.googlecloud.jsonlogging.logmanager.DefaultConsoleHandler} can be used
 * as the target of the {@code handlers} property key in {@link
 * java.util.logging.LogManager#readConfiguration(java.io.InputStream)}. This is particularly useful
 * when used in conjunction with frameworks other than Quarkus (such as Spring Boot). See the class
 * documentation for details.
 */
@NullMarked
package eu.mulk.quarkus.googlecloud.jsonlogging.logmanager;

import org.jspecify.annotations.NullMarked;
