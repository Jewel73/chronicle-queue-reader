package com.jewel.logreader;


/**
 * 
 * @author md.jewel
 * @date 31-31-2022
 *
 */
public class FileInfo {
	
	private String fileName;
	private long lastLogIndex;

	public FileInfo(){

	}
	
	public FileInfo(final String fileName, final long lastLogIndex) {
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

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public void setLastLogIndex(long index){
		this.lastLogIndex = index;
	}
	
	
}
