package com.naztech.logreader;

import org.jetbrains.annotations.NotNull;

/**
 * 
 * @author md.jewel
 * @date 31-31-2022
 *
 */
public class FileInfo {
	
	private String fileName;
	private long lastLogIndex;
	
	public FileInfo(@NotNull final String fileName,@NotNull final long lastLogIndex) {
		super();
		this.fileName = fileName;
		this.lastLogIndex = lastLogIndex;
	}

	public String getFileName() {
		return fileName;
	}

	public long getLastLogIndex() {
		return lastLogIndex;
	}
	
	
}
