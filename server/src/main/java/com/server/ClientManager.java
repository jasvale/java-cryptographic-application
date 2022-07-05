package com.server;

import com.commons.datastructures.ClientData;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Manager for client lists.
 */
public class ClientManager {
	private final Map<String, ClientData> clientDataByIdentifier = new HashMap<>();
	
	public ClientManager() {
		
	}
	
	public ClientData clientConnected(byte[] received, String ip)  {
		ClientData clientData = new ClientData(received, ip);
		this.addNewClient(clientData);
		return clientData;
	}
	
	public byte[] getFileList(ClientData clientRequesting) 
	{	
		String listForClient = "";
		for(Entry<String, ClientData> entry : this.clientDataByIdentifier.entrySet()) {
			
			if(clientRequesting.getIdentifier().equals(entry.getValue().getIdentifier()))
				continue;
			
			String fileList = entry.getValue().getFileList();
			String ipAddress = entry.getValue().getIp();
			listForClient += ipAddress;
			listForClient += "\n" + fileList;
		}
		if(listForClient.isBlank())
			listForClient = "I have nothing to show to you.";

		return listForClient.getBytes();
	}
	
	/**
	 * Add new connected client in the P2P network.
	 */
	private void addNewClient(ClientData clientData) {
		if(!this.clientDataByIdentifier.containsKey(clientData.getIdentifier())) {
			this.clientDataByIdentifier.put(clientData.getIdentifier(), clientData);
		}
	}
	
	/**
	 * Remove the client from the P2P network
	 */
	public void removeClient(ClientData clientData) {
		if(this.clientDataByIdentifier.containsKey(clientData.getIdentifier())) {
			this.clientDataByIdentifier.remove(clientData.getIdentifier());
		}
	}	
}
