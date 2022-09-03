// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.deployment;

import eu.mulk.quarkus.googlecloud.jsonlogging.runtime.GoogleCloudJsonLoggingConfiguration;
import eu.mulk.quarkus.googlecloud.jsonlogging.runtime.GoogleCloudJsonLoggingRecorder;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogConsoleFormatBuildItem;

/**
 * Registers {@link eu.mulk.quarkus.googlecloud.jsonlogging.Formatter} as the formatter for the
 * embedded JBoss Log Manager.
 */
public class GoogleCloudLoggingProcessor {

  private static final String FEATURE = "googlecloud-jsonlogging";

  /**
   * Returns the feature name of {@code "googlecloud-jsonlogging"}.
   *
   * @return the feature {@code "googlecloud-jsonlogging"}
   */
  @BuildStep
  public FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  /**
   * Constructs a {@link eu.mulk.quarkus.googlecloud.jsonlogging.Formatter} at runtime and returns
   * it.
   *
   * @param recorder the recorder that implements the construction process at runtime.
   * @return an instance of {@link eu.mulk.quarkus.googlecloud.jsonlogging.Formatter}.
   */
  @BuildStep
  @Record(ExecutionTime.RUNTIME_INIT)
  public LogConsoleFormatBuildItem setUpFormatter(
      GoogleCloudJsonLoggingRecorder recorder, GoogleCloudJsonLoggingConfiguration configuration) {
    return new LogConsoleFormatBuildItem(recorder.initialize(configuration));
  }
}
