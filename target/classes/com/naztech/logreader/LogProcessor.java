package com.naztech.logreader;

import org.jetbrains.annotations.Nullable;

import net.openhft.chronicle.logger.ChronicleLogLevel;

/**
 * 
 * @author md.jewel
 * date: 31/12/2022
 *
 */

public interface LogProcessor {
	
	void process(
			final String fileName,
			final long currentLogIndex,
            final long timestamp,
            final ChronicleLogLevel level,
            final String loggerName,
            final String threadName,
            final String message,
            @Nullable final Throwable throwable,
            final Object[] args);

}
