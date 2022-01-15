package eu.mulk.quarkus.googlecloud.jsonlogging.example;

import eu.mulk.quarkus.googlecloud.jsonlogging.KeyValueParameter;
import eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameter;
import eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameterProvider;
import io.quarkus.arc.Unremovable;
import java.util.concurrent.ThreadLocalRandom;
import javax.inject.Singleton;

@Singleton
@Unremovable
public class RandomNumberParameterProvider implements StructuredParameterProvider {

  @Override
  public StructuredParameter getParameter() {
    return KeyValueParameter.of("randomNumber", ThreadLocalRandom.current().nextInt());
  }
}
