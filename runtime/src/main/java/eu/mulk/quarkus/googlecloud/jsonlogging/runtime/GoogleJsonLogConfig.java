package eu.mulk.quarkus.googlecloud.jsonlogging.runtime;

import io.quarkus.runtime.annotations.*;

/**
 * Configuration for Google JSON log formatting.
 */
@ConfigRoot(phase = ConfigPhase.RUN_TIME, name = "log.console")
public class GoogleJsonLogConfig {

    /**
     * Console google json logging.
     */
    @ConfigDocSection
    @ConfigItem(name = "json.google")
    JsonConfig jsonGoogle;

    @ConfigGroup
    public static class JsonConfig {

        /**
         * Determine whether to enable the JSON console formatting extension, which disables "normal" console formatting.
         */
        @ConfigItem(name = ConfigItem.PARENT, defaultValue = "true")
        boolean enable;

    }
}