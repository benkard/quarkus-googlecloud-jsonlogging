// SPDX-FileCopyrightText: © 2022 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.runtime;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithParentName;

/** Configuration for console logging in Google Cloud Logging JSON format. */
@ConfigMapping(prefix = "quarkus.log.console.google")
@ConfigRoot(phase = RUN_TIME)
public interface GoogleCloudJsonLoggingConfiguration {

  /**
   * Whether to enable Google Cloud Logging JSON logging to <code>stdout</code>/<code>stderr</code>.
   *
   * <p>Replaces the regular plain-text format for console logs.
   */
  @WithDefault("true")
  @WithParentName
  boolean enabled();
}
