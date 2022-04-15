// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

/**
 * Provides structured logging to standard output according to the Google Cloud Logging
 * specification.
 *
 * <ul>
 *   <li><a href="#sect-summary">Summary</a>
 *   <li><a href="#sect-activation">Activation</a>
 *   <li><a href="#sect-usage">Usage</a>
 * </ul>
 *
 * <h2 id="sect-summary">Summary</h2>
 *
 * <p>This package contains a log formatter for JBoss Logging in the form of a Quarkus plugin that
 * implements the <a href="https://cloud.google.com/logging/docs/structured-logging">Google Cloud
 * Logging JSON format</a> on standard output.
 *
 * <p>It is possible to log unstructured text, structured data, or a mixture of both depending on
 * the situation.
 *
 * <h2 id="sect-activation">Activation</h2>
 *
 * <ul>
 *   <li><a href="#sect-activation-maven">Activation with Maven</a>
 *   <li><a href="#sect-activation-gradle">Activation with Gradle</a>
 * </ul>
 *
 * <p>Add the runtime POM to your dependency list. As long as the JAR is on the classpath at both
 * build time and runtime, the log formatter automatically registers itself on startup.
 *
 * <h3 id="sect-activation-maven">Activation with Maven</h3>
 *
 * {@snippet lang="xml" :
 * <project>
 *   ...
 *
 *   <dependencies>
 *     ...
 *
 *     <dependency>
 *       <groupId>eu.mulk.quarkus-googlecloud-jsonlogging</groupId>
 *       <artifactId>quarkus-googlecloud-jsonlogging</artifactId>
 *       <version>4.0.0</version>
 *     </dependency>
 *
 *     ...
 *   </dependencies>
 *
 *   ...
 * </project>
 * }
 *
 * <h3 id="sect-activation-gradle">Activation with Gradle</h3>
 *
 * {@snippet lang="groovy" :
 * dependencies {
 *   // ...
 *
 *   implementation("eu.mulk.quarkus-googlecloud-jsonlogging:quarkus-googlecloud-jsonlogging:4.0.0")
 *
 *   // ...
 * }
 * }
 *
 * <h2 id="sect-usage">Usage</h2>
 *
 * <p>See the documentation of the {@link eu.mulk.quarkus.googlecloud.jsonlogging} package for usage
 * instructions.
 *
 * @see eu.mulk.quarkus.googlecloud.jsonlogging
 */
package eu.mulk.quarkus.googlecloud.jsonlogging.runtime;
