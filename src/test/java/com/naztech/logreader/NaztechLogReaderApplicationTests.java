package com.naztech.logreader;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.naztech.logreader.processor.KafkaProcessor;
import com.naztech.logreader.service.CustomLogReaderService;


@SpringBootTest
class NaztechLogReaderApplicationTests {
	
	String queuePath = "C:\\Users\\md.jewel\\Desktop\\Java\\chronicle-logger-test\\logs\\path\\";
	
	@Test
	public void testLogReader() throws Exception {
		//(CYCLE_STR.equals("TODAY")) {
		
           System.out.println( TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()));
		
		
		
		CustomLogReaderService customLogReaderService = new CustomLogReaderService(queuePath, queuePath, queuePath);
		
		try {
			customLogReaderService.readLogs(new KafkaProcessor()::process, true);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}

