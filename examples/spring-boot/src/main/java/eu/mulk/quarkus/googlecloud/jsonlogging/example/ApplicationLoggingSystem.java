package eu.mulk.quarkus.googlecloud.jsonlogging.example;

import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.Slf4JLoggingSystem;

public class ApplicationLoggingSystem extends Slf4JLoggingSystem {

  public ApplicationLoggingSystem(ClassLoader classLoader) {
    super(classLoader);
  }

  @Override
  protected String[] getStandardConfigLocations() {
    return new String[0];
  }

  @Override
  protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile) {
    /* no configuration necessary */
  }
}
