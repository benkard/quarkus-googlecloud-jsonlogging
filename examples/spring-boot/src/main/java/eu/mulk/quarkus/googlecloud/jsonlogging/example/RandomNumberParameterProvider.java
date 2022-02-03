// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: GPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.example;

import eu.mulk.quarkus.googlecloud.jsonlogging.KeyValueParameter;
import eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameter;
import eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameterProvider;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumberParameterProvider implements StructuredParameterProvider {

  @Override
  public StructuredParameter getParameter() {
    return KeyValueParameter.of("randomNumber", ThreadLocalRandom.current().nextInt());
  }
}
