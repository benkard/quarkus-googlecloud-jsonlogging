// SPDX-FileCopyrightText: © 2022 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

/** Configuration for console logging in Google Cloud Logging JSON format. */
@ConfigRoot(prefix = "quarkus.log.console", name = "google", phase = ConfigPhase.RUN_TIME)
public class GoogleCloudJsonLoggingConfiguration {

  /**
   * Whether to enable Google Cloud Logging JSON logging to <code>stdout</code>/<code>stderr</code>.
   *
   * <p>Replaces the regular plain-text format for console logs.
   */
  @ConfigItem(defaultValue = "true", name = ConfigItem.PARENT)
  public boolean enabled;
}
