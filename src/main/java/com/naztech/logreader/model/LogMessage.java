package com.naztech.logreader.model;

import net.openhft.chronicle.logger.ChronicleLogLevel;

public class LogMessage {
    private String fileName;
    private long currentLogIndex;
    private long timestamp;
    private ChronicleLogLevel level;
    private String threadName;
    private String loggerName;
    private String message;
    private Throwable throwable;
    private Object[] args;

    // Constructors, getters, and setters for the fields

    public LogMessage(String fileName, long currentLogIndex, long timestamp, ChronicleLogLevel level,
                      String threadName, String loggerName, String message, Throwable throwable, Object[] args) {
        this.fileName = fileName;
        this.currentLogIndex = currentLogIndex;
        this.timestamp = timestamp;
        this.level = level;
        this.threadName = threadName;
        this.loggerName = loggerName;
        this.message = message;
        this.throwable = throwable;
        this.args = args;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getCurrentLogIndex() {
        return currentLogIndex;
    }

    public void setCurrentLogIndex(long currentLogIndex) {
        this.currentLogIndex = currentLogIndex;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ChronicleLogLevel getLevel() {
        return level;
    }

    public void setLevel(ChronicleLogLevel level) {
        this.level = level;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
