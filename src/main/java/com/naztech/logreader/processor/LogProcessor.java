package com.naztech.logreader.processor;

import org.jetbrains.annotations.Nullable;

import com.naztech.logreader.model.LogMessage;

import net.openhft.chronicle.logger.ChronicleLogLevel;

/**
 * 
 * @author md.jewel
 * date: 31/12/2022
 *
 */

public interface LogProcessor {
	
	/**
	 * process method need to be overridden to process the logs data
	 * 
	 * @param fileName - current log file name
	 * @param currentLogIndex - current index number of the reading log
	 * @param timestamp - time of the log has been written
	 * @param level - log level
	 * @param loggerName - logger name
	 * @param threadName - method name
	 * @param message - main log message
	 * @param throwable - 
	 * @param args - additional logs info
	 */
	
	void process(LogMessage logMessage);
	
	//void process(LogMessage logMessage);

}
