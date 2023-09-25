package com.jewel.logreader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;


import net.openhft.chronicle.logger.ChronicleLogLevel;
import net.openhft.chronicle.logger.tools.ChronicleLogProcessor;
import net.openhft.chronicle.logger.tools.ChronicleLogReader;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.wire.DocumentContext;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.WireType;



/**
 * 
 * @author md.jewel
 * @date 31-12-31
 *
 */

public class CustomChronicleLogReader {
	

    private static final SimpleDateFormat tsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final ChronicleQueue cq;

    /**
     * Create reader with default wire type
     *
     * @param path the path to Chronicle Logs storage
     */
    public CustomChronicleLogReader(
            @NotNull String path) {
        this(path, WireType.BINARY_LIGHT, RollCycles.DAILY);
    }

    /**
     * @param path     the path to Chronicle Logs storage
     * @param wireType Chronicle wire type. Must match the wire type specified in corresponding Chronicle Logger
     */
    public CustomChronicleLogReader(
            @NotNull String path,
            @NotNull WireType wireType, 
            RollCycles cycle) {
        cq = ChronicleQueue.singleBuilder(path).wireType(wireType).rollCycle(cycle).build();
        
        
    }

    /**
     * @author md.jewel
     * 
     */
    public static String getFromatedLog(
            long timestamp,
            ChronicleLogLevel level,
            String loggerName,
            String threadName,
            String message,
            @Nullable Throwable throwable,
            Object[] args) {

        message = MessageFormatter.arrayFormat(message, args).getMessage();

        if (throwable == null) {
            return String.format("%s [%s] [%s] [%s] %s%n",
                    tsFormat.format(timestamp),
                    level.toString(),
                    threadName,
                    loggerName,
                    message);

        } else {
        	return String.format("%s [%s] [%s] [%s] %s%n%s%n",
                    tsFormat.format(timestamp),
                    level.toString(),
                    threadName,
                    loggerName,
                    message,
                    throwable.toString());
        }
    }

    /**
     * @author md.jewel
     * 
     * Decode logs
     *
     * @param processor user-provided processor called for each log message
     * @param waitForIt whether to wait for more data or stop after EOF reached
     * @param lastReadFile last read files information - to start the reading logs where it was left off
     */
    public void processLogs(@NotNull LogProcessor processor, boolean waitForIt, FileInfo lastReadFile) {
        ExcerptTailer tailer = cq.createTailer();
        moveToLastReadLogPosition(tailer, lastReadFile);
        
        for (; ; ) {
            try (DocumentContext dc = tailer.readingDocument()) {
                Wire wire = dc.wire();
                if (wire == null) {
                	if (waitForIt) {
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException ignored) {

                        }
                        continue;
                    } else {
                        break;
                    }
                }
                extractWireData(tailer, wire, processor);
            }
        }
    }
    
    /**
     * @author md.jewel
     * 
     * Move queue log reading index position where it was left off
     *
     */
    
    private static void moveToLastReadLogPosition(ExcerptTailer tailer, FileInfo lastReadFile) {
    	if(lastReadFile != null && !lastReadFile.getFileName().isEmpty()) {
    		String lastFile = lastReadFile.getFileName();
    		while(tailer.readText() != null) {
    			if(tailer.currentFile().getName().equalsIgnoreCase(lastFile)) {
    				tailer.moveToIndex(lastReadFile.getLastLogIndex());
    	    		break;
    			}
    			continue;
    			
    		}
    	}
    }
    
    /**
     * @author md.jewel
     * 
     * Extract logs msg line by line using Wire deserialization
     * @param processor call user-provided processor method with the logs value
     *
     */
    
    private static void extractWireData( ExcerptTailer tailer, Wire wire, LogProcessor processor) {
    	// get tailer information
        String fileName = tailer.currentFile().getName();
        long currentLogIndex = tailer.index();
        // get log information
        long timestamp = wire.read("ts").int64();
        ChronicleLogLevel level = wire.read("level").asEnum(ChronicleLogLevel.class);
        String threadName = wire.read("threadName").text();
        String loggerName = wire.read("loggerName").text();
        String message = wire.read("message").text();
        Throwable th = wire.hasMore() ? wire.read("throwable").throwable(false) : null;
        
        //custom log read from here
        List<Object> argsL = new ArrayList<>();
        if (wire.hasMore()) {
            wire.read("args").sequence(argsL, (l, vi) -> {
                while (vi.hasNextSequenceItem()) {
                    l.add(vi.object(Object.class));
                }
            });
        }
        Object[] args = argsL.toArray(new Object[argsL.size()]);
        processor.process(fileName, currentLogIndex, timestamp, level, threadName, loggerName, message, th, args);
    }



}
