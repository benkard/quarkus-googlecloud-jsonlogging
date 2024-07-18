// SPDX-FileCopyrightText: © 2022 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: LGPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging;

import org.jboss.logmanager.ExtLogRecord;
import org.jspecify.annotations.Nullable;

/**
 * Contextual data available to {@link StructuredParameterProvider} and {@link LabelProvider}.
 *
 * <p>Provides access to information carried by the {@link ExtLogRecord} that is being formatted and
 * that is not taken care of by {@link Formatter} by default.
 */
public interface ProviderContext {

  /**
   * The {@link ExtLogRecord#getLoggerName()} property of the log record that is being formatted.
   *
   * @return {@link ExtLogRecord#getLoggerName()}.
   */
  @Nullable String loggerName();

  /**
   * The {@link ExtLogRecord#getSequenceNumber()} property of the log record that is being
   * formatted.
   *
   * @return {@link ExtLogRecord#getSequenceNumber()}.
   */
  long sequenceNumber();

  /**
   * The {@link ExtLogRecord#getThreadName()} property of the log record that is being formatted.
   *
   * @return {@link ExtLogRecord#getThreadName()}.
   */
  @Nullable String threadName();
}
