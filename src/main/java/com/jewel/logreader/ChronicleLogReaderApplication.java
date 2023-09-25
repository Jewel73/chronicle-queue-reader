package com.jewel.logreader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.jewel.logreader.processor.KafkaProcessor;
import com.jewel.logreader.service.CustomLogReaderService;


@SpringBootApplication
@ComponentScan(basePackages = "com.naztech")
public class ChronicleLogReaderApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChronicleLogReaderApplication.class, args);
		
	}
	
	@Autowired
	private CustomLogReaderService customLogReaderService;

	@Override
	public void run(String... args) throws Exception {
		
		customLogReaderService.readLogs(new KafkaProcessor()::process, true);
		
	}

}
