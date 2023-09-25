package com.jewel.logreader.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jewel.logreader.constant.QueueConstant;
import com.jewel.logreader.constant.WireKeys;
import com.jewel.logreader.exception.FileIndexNotFoundException;
import com.jewel.logreader.model.FileInfo;
import com.jewel.logreader.model.LogMessage;
import com.jewel.logreader.processor.LogProcessor;

import net.openhft.chronicle.logger.ChronicleLogLevel;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.wire.DocumentContext;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.WireKey;
import net.openhft.chronicle.wire.WireType;


/**
 * CustomLogReaderService is a service for reading logs from a Chronicle Queue.
 *
 * This service provides methods for reading logs, decoding log messages, and more.
 * 
 * @author Md Jewel
 * @Date 22-09-2023
 * @version 1.0
 */

@Service
public class CustomLogReaderService {
	
	private String QUEUE_TOPIC;
	private String CYCLE_STR;
	private String QUEUE_PATH;
    private final ChronicleQueue cq;

    
    @Autowired
	public CustomLogReaderService(
			
		@Value("${base.queue.path}") String baseQueuePath,
		@Value("${queue.topic: defulatTopic}") String queueTopic,
		@Value("${queue.start.cycle:}") String startCycle
			 
	) {
    	this.QUEUE_PATH 	= baseQueuePath;
    	this.QUEUE_TOPIC 	= queueTopic;
    	this.CYCLE_STR  	= startCycle;
    	
        cq = ChronicleQueue.singleBuilder(QUEUE_PATH).wireType(WireType.BINARY_LIGHT)
        	.rollCycle(RollCycles.DAILY)
        	.build();
    }


    /**
     * Decode logs
     *
     * @param processor user-provided processor called for each log message
     * @param waitForIt whether to wait for more data or stop after EOF reached
     * @param lastReadFile last read files information - to start the reading logs where it was left off
     * @throws Exception 
     */
    
    public void readLogs(@NotNull LogProcessor processor, boolean waitForIt) throws Exception {
    	
        ExcerptTailer tailer = createTailer();
        moveToCycle(tailer);
        
        while (true) {
            try (DocumentContext dc = tailer.readingDocument()) {
                Wire wire = dc.wire();
                if (wire == null) {
                    if (waitForIt) {
                        Thread.sleep(50L);
                        continue;
                    } else {
                        break;
                    }
                }
                
                var log = extractWireData(tailer, wire, processor);
                processor.process(log);
            } catch (Exception e) {
                 throw new Exception("Reading wire data failed, error message+ "+e.getLocalizedMessage());
            }
        }
    }
    
 
    
    /**
     * Creates and returns an ExcerptTailer for the specified queue topic.
     *
     * @return An ExcerptTailer configured for the specified queue topic.
     */
    
    private ExcerptTailer createTailer() {
    	return cq.createTailer(QUEUE_TOPIC);
	}


    /**
     * Moves the provided ExcerptTailer to a specific cycle based on the CYCLE_STR parameter.
     *
     * @param tailer The ExcerptTailer to be positioned.
     */
    
	public void moveToCycle(ExcerptTailer tailer) {
		int time;
		
		if (CYCLE_STR == null || CYCLE_STR.isBlank()) return;
        if (CYCLE_STR.equals(QueueConstant.TODAY)) {
            time = (int) TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
        } else {
            time = (int) LocalDate.parse(CYCLE_STR, DateTimeFormatter.ofPattern(QueueConstant.TIME_FORMAT, Locale.US)).toEpochDay();
        }
        
        tailer.moveToCycle(time);
    }
    
	
    /**
     * 
     * Extract logs message line by line using Wire deserialization
     * @param processor: call user-provided processor method with the logs value
     *
     */
    
    private static LogMessage extractWireData( ExcerptTailer tailer, Wire wire, LogProcessor processor) {
    	
        String fileName = tailer.currentFile().getName();
        long currentLogIndex = tailer.index();
        long timestamp = wire.read(WireKeys.TIMESTAMP).int64();
        ChronicleLogLevel level = wire.read(WireKeys.LEVEL).asEnum(ChronicleLogLevel.class);
        String threadName = wire.read(WireKeys.THREADNAME).text();
        String loggerName = wire.read(WireKeys.LOGGERNAME).text();
        String message = wire.read(WireKeys.MESSAGE).text();
        Throwable th = wire.hasMore() ? wire.read(WireKeys.THROWABLE).throwable(false) : null;
        
        List<Object> argsL = new ArrayList<>();
        if (wire.hasMore()) {
            wire.read(WireKeys.ARGS).sequence(argsL, (l, vi) -> {
                while (vi.hasNextSequenceItem()) {
                    l.add(vi.object(Object.class));
                }
            });
        }
        Object[] args = argsL.toArray(new Object[argsL.size()]);
        
        LogMessage logMessage = new LogMessage(fileName, currentLogIndex, timestamp, level,
                threadName, loggerName, message, th, args);
        
        return logMessage;
    }
    
    
    /**
     * 
     * Move queue log reading index position 
     * @throws FileIndexNotFoundException 
     *
     */
    
    private static void moveToLastReadLogPosition(ExcerptTailer tailer, FileInfo lastReadFile) throws FileIndexNotFoundException {
    
    	if(lastReadFile != null && !lastReadFile.getFileName().isEmpty()) {
    		if(tailer.moveToIndex(lastReadFile.getLastLogIndex())){
    			return;
    		}else {
    			throw new FileIndexNotFoundException("Exception occured, File index not found");
    		}
    	}
    }
    



}
