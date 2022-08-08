// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.runtime;

import eu.mulk.quarkus.googlecloud.jsonlogging.Formatter;
import eu.mulk.quarkus.googlecloud.jsonlogging.LabelProvider;
import eu.mulk.quarkus.googlecloud.jsonlogging.ParametrizedStructuredParameterProvider;
import eu.mulk.quarkus.googlecloud.jsonlogging.StructuredParameterProvider;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/** A Quarkus recorder that registers {@link Formatter} as a log formatter for the application. */
@Recorder
public class GoogleCloudJsonLoggingRecorder {

  /**
   * Registers {@link Formatter} as a log formatter for the application.
   *
   * <p>Collects all discoverable {@link StructuredParameterProvider}s, {@link ParametrizedStructuredParameterProvider}s
   * and {@link LabelProvider}s and passes them to {@link Formatter#Formatter(Collection, Collection, Collection)}.
   *
   * @return the registered {@link Formatter}.
   */
  public RuntimeValue<Optional<java.util.logging.Formatter>> initialize(GoogleJsonLogConfig config) {
    if(!config.jsonGoogle.enable) {
      return new RuntimeValue<>(Optional.empty());
    }

    var parameterProviders =
            Arc.container().select(StructuredParameterProvider.class).stream()
                    .collect(Collectors.toList());

    var parametrizedParameterProviders =
            Arc.container().select(ParametrizedStructuredParameterProvider.class).stream()
                    .collect(Collectors.toList());

    var labelProviders =
            Arc.container().select(LabelProvider.class).stream().collect(Collectors.toList());

    return new RuntimeValue<>(Optional.of(Formatter.load(parameterProviders, parametrizedParameterProviders, labelProviders)));
  }

}
