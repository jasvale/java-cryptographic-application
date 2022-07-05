package com.commons.utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileCrawler
{
	private StringBuilder builder = new StringBuilder();
	private int fileIndexer = 0;
	private Map<Integer, String> filesAndIds = new HashMap<>();
	
	public FileCrawler(String identifier) throws IOException
	{
		// Open the directory path
		File folder = new File(com.commons.utilities.Paths.sharedFiles +identifier+"\\");
		
		// Get the file list
		File[] files = folder.listFiles();
		
		if(files.length == 0)
			return ;
		
		// Padding for first level
		String padding = "  ";

		// For each file or directory inside the path
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String fileName = files[i].getName();
				build(" -("+this.fileIndexer +")- " + fileName + " - " + files[i].length()/1024 + "Kb");
				this.filesAndIds.put(this.fileIndexer, files[i].getCanonicalPath());
				this.fileIndexer++;
			}
			else if (files[i].isDirectory()) {
				String directoryName = files[i].getName();
				if(directoryName.equals("logs")) {
					continue;
				}
				else if(directoryName.equals("downloaded")) {
					continue;
				}
				listFiles(files[i], padding);
			}
		}
	}

	public void listFiles(File folder, String padding) throws IOException
	{
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
			{
				String fileName = files[i].getName();
				build(padding+" -("+this.fileIndexer +")- " + fileName  + " - " + files[i].length()/1024 + "Kb");
				this.filesAndIds.put(this.fileIndexer, files[i].getCanonicalPath());
				this.fileIndexer++;
			}
			else if (files[i].isDirectory()) {
				String subPadding = padding + padding;
				listFiles(files[i], subPadding);
			}
		}
	}
	
	private void build(String message) {
		this.builder.append(message+"\n");
	}
	
	public Map<Integer, String> getFilesAndIds() {
		return this.filesAndIds;
	}
	
	public String getFileList() {
		
		if(this.builder.toString().length() == 0)
			return "	No files";
		
		String list = this.builder.toString();
		list = list.substring(0, list.lastIndexOf("\n"));		
		return list;
	}
}