package com.jewel.logreader.processor;

import java.text.SimpleDateFormat;

import org.jetbrains.annotations.Nullable;

import com.jewel.logreader.model.LogMessage;
import com.jewel.logreader.utils.LogUtils;

import net.openhft.chronicle.logger.ChronicleLogLevel;

public class KafkaProcessor implements LogProcessor{

	@Override
	public void process(LogMessage logMessage) {
		
		LogUtils.printLog(logMessage);
		System.out.println();
		
	}
}
