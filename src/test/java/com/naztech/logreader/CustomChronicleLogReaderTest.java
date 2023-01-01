package com.naztech.logreader;

import org.junit.Test;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.wire.WireType;

/**
 * 
 * @author md.jewel
 * @date 01-01-2023
 */

public class CustomChronicleLogReaderTest {
	String directory = "C:\\Users\\md.jewel\\Desktop\\Java\\chron-logback\\library\\chronicle";
	

	/**
	 * read logs data from all files, stop after reading all data
	 * @param pass process instance in the processLogs function, 
	 * implements ProcessLog interface 
	 * @param pass whatever processor you want that implements LogProcessor
	 */
	
	@Test
	public void minimumConfigureProcessor() {
		CustomChronicleLogReader reader = new CustomChronicleLogReader(directory);
		reader.processLogs(new KafkaProcessor()::process, false, null);
	}
	
	
	/**
	 * 
	 * read data from specific file, from specific position 
	 * @param pass true in the processLogs function to read continuously
	 * @param pass whatever processor you want that implements LogProcessor
	 * 
	 */
	
	@Test
	public void configureProcessor() {
		FileInfo lastReadFile = new FileInfo("20230101F.cq4", 83141976915982L);
		WireType wt = WireType.BINARY_LIGHT;
		CustomChronicleLogReader reader = new CustomChronicleLogReader(directory, wt, RollCycles.DAILY);
		
		reader.processLogs(new KafkaProcessor()::process, false, lastReadFile);
	}
	
	/**
	 * read data continuously
	 * 
	 */
	
	//@Test
	public void contineousLogReader() {
		
		WireType wt = WireType.BINARY_LIGHT;
		CustomChronicleLogReader reader = new CustomChronicleLogReader(directory, wt, RollCycles.DAILY);
		
		reader.processLogs(new KafkaProcessor()::process, true, null);
	}

}



