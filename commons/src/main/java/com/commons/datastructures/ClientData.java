package com.commons.datastructures;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.commons.utilities.FileCrawler;

public class ClientData {

	private final String identifier;
	private String ip;
	private final String fileList;
	private Map<Integer, String> filesAndIds;
	
	public ClientData(byte[] received, String ip)
	{
		String[] splitted = new String(received, StandardCharsets.UTF_8).split(":");
		this.identifier = splitted[0];
		this.fileList = splitted[1];
		this.ip = ip;
	}

	public ClientData(String identifier) throws IOException {
		FileCrawler fileCrawler = new FileCrawler(identifier);
		this.fileList = fileCrawler.getFileList();
		this.filesAndIds = fileCrawler.getFilesAndIds();
		this.identifier = identifier;
	}
	
	public byte[] getData() {
		return (this.identifier+":"+this.fileList).getBytes();
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public String getIp() {
		return ip;
	}
	
	public String getFileList() {
		return this.fileList;
	}
	
	public Map<Integer, String> getFilesAndIds() {
		return this.filesAndIds;
	}
}
