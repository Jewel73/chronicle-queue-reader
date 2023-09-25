package com.naztech.logreader.processor;

import java.text.SimpleDateFormat;

import org.jetbrains.annotations.Nullable;
import com.naztech.logreader.model.LogMessage;
import com.naztech.logreader.utils.LogUtils;

import net.openhft.chronicle.logger.ChronicleLogLevel;

public class KafkaProcessor implements LogProcessor{

	@Override
	public void process(LogMessage logMessage) {
		
		LogUtils.printLog(logMessage);
		System.out.println();
		
	}
}
