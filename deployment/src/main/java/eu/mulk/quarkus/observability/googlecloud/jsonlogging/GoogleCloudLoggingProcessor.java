package eu.mulk.quarkus.observability.googlecloud.jsonlogging;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogConsoleFormatBuildItem;

class GoogleCloudLoggingProcessor {

  private static final String FEATURE = "googlecloud-jsonlogging";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  @Record(ExecutionTime.RUNTIME_INIT)
  LogConsoleFormatBuildItem setUpFormatter(GoogleCloudLoggingRecorder recorder) {
    return new LogConsoleFormatBuildItem(recorder.initialize());
  }
}
