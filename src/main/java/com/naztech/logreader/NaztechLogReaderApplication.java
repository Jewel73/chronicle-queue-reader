package com.naztech.logreader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.naztech.logreader.processor.KafkaProcessor;
import com.naztech.logreader.service.CustomLogReaderService;


@SpringBootApplication
@ComponentScan(basePackages = "com.naztech")
public class NaztechLogReaderApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(NaztechLogReaderApplication.class, args);
		
	}
	
	@Autowired
	private CustomLogReaderService customLogReaderService;

	@Override
	public void run(String... args) throws Exception {
		
		customLogReaderService.readLogs(new KafkaProcessor()::process, true);
		
	}

}
