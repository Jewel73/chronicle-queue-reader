package com.naztech.logreader;

import org.jetbrains.annotations.Nullable;

import net.openhft.chronicle.logger.ChronicleLogLevel;

public class KafkaProcessor implements LogProcessor{

	@Override
	public void process(String fileName, long currentLogIndex, long timestamp, ChronicleLogLevel level,
			String loggerName, String threadName, String message, @Nullable Throwable throwable, Object[] args) {
		
		// configure your kafka producer 
		System.out.printf("Sending to kafka: %s %d %d %s %s %s", fileName, currentLogIndex, timestamp, level, threadName, message);
		System.out.println();
		
	}

}
