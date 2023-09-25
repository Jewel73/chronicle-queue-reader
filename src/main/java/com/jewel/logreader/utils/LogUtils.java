package com.jewel.logreader.utils;

import java.text.SimpleDateFormat;

import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import com.jewel.logreader.model.LogMessage;

import net.openhft.chronicle.logger.ChronicleLogLevel;

public class LogUtils {

    private static final SimpleDateFormat tsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	
	/**
     * 
     * print every line of log in formatted way
     * 
     */
    
    public static void printLog(
    		LogMessage logMessage) {

        logMessage.setMessage(MessageFormatter.arrayFormat(logMessage.getMessage(), logMessage.getArgs()).getMessage());
        
        if (logMessage.getThrowable() == null) {
            System.out.printf("%s [%d] %s [%s] [%s] [%s] %s%n",
            		logMessage.getFileName(),
            		logMessage.getCurrentLogIndex(),
                    tsFormat.format(logMessage.getTimestamp()),
                    logMessage.getLevel().toString(),
                    logMessage.getThreadName(),
                    logMessage.getLoggerName(),
                    logMessage.getMessage());

        } else {
        	System.out.printf("%s [%d] %s [%s] [%s] [%s] %s%n%s%n",
        			logMessage.getFileName(),
            		logMessage.getCurrentLogIndex(),
                    tsFormat.format(logMessage.getTimestamp()),
                    logMessage.getLevel().toString(),
                    logMessage.getThreadName(),
                    logMessage.getLoggerName(),
                    logMessage.getMessage(),
                    logMessage.getThrowable().toString());
        }
    }

}
