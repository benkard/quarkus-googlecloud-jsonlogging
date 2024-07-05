// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

/**
 * Provides structured logging to standard output according to the Google Cloud Logging
 * specification.
 *
 * <ul>
 *   <li><a href="#sect-summary">Summary</a>
 *   <li><a href="#sect-installation">Installation</a>
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
 * <h2 id="sect-installation">Installation</h2>
 *
 * <ul>
 *   <li><a href="#sect-installation-maven">Installation with Maven</a>
 *   <li><a href="#sect-installation-gradle">Installation with Gradle</a>
 * </ul>
 *
 * <p>Add the runtime POM to your dependency list. As long as the JAR is on the classpath at both
 * build time and runtime, the log formatter automatically registers itself on startup.
 *
 * <h3 id="sect-installation-maven">Installation with Maven</h3>
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
 *       <artifactId>quarkus-googlecloud-jsonlogging-core</artifactId>
 *       <version>6.5.0</version>
 *     </dependency>
 *
 *     ...
 *   </dependencies>
 *
 *   ...
 * </project>
 * }
 *
 * <h3 id="sect-installation-gradle">Installation with Gradle</h3>
 *
 * {@snippet lang="groovy" :
 * dependencies {
 *   // ...
 *
 *   implementation("eu.mulk.quarkus-googlecloud-jsonlogging:quarkus-googlecloud-jsonlogging-core:6.5.0")
 *
 *   // ...
 * }
 * }
 *
 * <h2 id="sect-usage">Usage</h2>
 *
 * <ul>
 *   <li><a href="#sect-usage-parameter">Using Label and StructuredParameter</a>
 *   <li><a href="#sect-usage-provider">Using LabelProvider and StructuredParameterProvider</a>
 *   <li><a href="#sect-usage-mdc">Using the Mapped Diagnostic Context</a>
 * </ul>
 *
 * <p>Logging unstructured data requires no code changes. All logs are automatically converted to
 * Google-Cloud-Logging-compatible JSON.
 *
 * <p>Structured data can be logged in one of 3 different ways: by passing {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.Label}s and {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameter}s as parameters to individual log
 * entries, by supplying {@link eu.mulk.quarkus.googlecloud.jsonlogging.LabelProvider}s and {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameterProvider}s, or by using the Mapped
 * Diagnostic Context.
 *
 * <h3 id="sect-usage-parameter">Using Label and StructuredParameter</h3>
 *
 * <p>Instances of {@link eu.mulk.quarkus.googlecloud.jsonlogging.Label} and {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameter} can be passed as log parameters to
 * the {@code *f} family of logging functions on JBoss Logging's {@link org.jboss.logging.Logger}.
 *
 * <p>Simple key–value pairs are represented by {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.KeyValueParameter}.
 *
 * <p><strong>Example:</strong>
 *
 * {@snippet :
 * logger.logf(
 *   "Request rejected: unauthorized.",
 *   Label.of("requestId", "123"),
 *   KeyValueParameter.of("resource", "/users/mulk"),
 *   KeyValueParameter.of("method", "PATCH"),
 *   KeyValueParameter.of("reason", "invalid token"));
 * }
 *
 * <p>Result:
 *
 * {@snippet lang="json" :
 * {
 *   "jsonPayload": {
 *     "message": "Request rejected: unauthorized.",
 *     "resource": "/users/mulk",
 *     "method": "PATCH",
 *     "reason": "invalid token"
 *   },
 *   "labels": {
 *     "requestId": "123"
 *   }
 * }
 * }
 *
 * <h3 id="sect-usage-provider">Using LabelProvider and StructuredParameterProvider</h3>
 *
 * <p>If you pass {@link eu.mulk.quarkus.googlecloud.jsonlogging.LabelProvider}s and {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameterProvider}s to {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.Formatter}, then they are consulted to provide labels and
 * parameters for each message that is logged. This can be used to provide contextual information
 * such as tracing and request IDs stored in thread-local storage.
 *
 * <p><strong>Service provider support:</strong> Providers can be registered using the {@link
 * java.util.ServiceLoader} mechanism, in which case {@link
 * eu.mulk.quarkus.googlecloud.jsonlogging.Formatter#load} picks them up automatically.
 *
 * <p><strong>CDI support:</strong> If you are using the Quarkus extension, CDI beans that implement
 * one of the provider interfaces are automatically detected at build time and passed to the
 * formatter on startup. In addition, providers using the {@link java.util.ServiceLoader} mechanism
 * are detected and passed to the formatter as well.
 *
 * <p><strong>Example:</strong>
 *
 * {@snippet :
 * @Singleton
 * @Unremovable
 * public final class TraceLogParameterProvider implements StructuredParameterProvider, LabelProvider {
 *
 *   @Override
 *   public StructuredParameter getParameter() {
 *     var b = Json.createObjectBuilder();
 *     b.add("traceId", Span.current().getSpanContext().getTraceId());
 *     b.add("spanId", Span.current().getSpanContext().getSpanId());
 *     return () -> b;
 *   }
 *
 *   @Override
 *   public Collection<Label> getLabels() {
 *     return List.of(Label.of("requestId", "123"));
 *   }
 * }
 * }
 *
 * Result:
 *
 * {@snippet lang="json" :
 * {
 *   "jsonPayload": {
 *     "message": "Request rejected: unauthorized.",
 *     "traceId": "39f9a49a9567a8bd7087b708f8932550",
 *     "spanId": "c7431b14630b633d"
 *   },
 *   "labels": {
 *     "requestId": "123"
 *   }
 * }
 * }
 *
 * <h3 id="sect-usage-mdc">Using the Mapped Diagnostic Context</h3>
 *
 * <p>Any key–value pairs in JBoss Logging's thread-local {@link org.jboss.logging.MDC} are added to
 * the resulting JSON.
 *
 * <p><strong>Example:</strong>
 *
 * {@snippet :
 * MDC.put("resource", "/users/mulk");
 * MDC.put("method", "PATCH");
 * logger.logf("Request rejected: unauthorized.");
 * }
 *
 * Result:
 *
 * {@snippet lang="json" :
 * {
 *   "jsonPayload": {
 *     "message": "Request rejected: unauthorized.",
 *     "resource": "/users/mulk",
 *     "method": "PATCH"
 *   }
 * }
 * }
 */
package eu.mulk.quarkus.googlecloud.jsonlogging;
