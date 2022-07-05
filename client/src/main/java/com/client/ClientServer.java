package com.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.commons.channel.ChannelFactory;
import com.commons.channel.IChannel;
import com.commons.datastructures.ClientData;
import com.commons.enums.EndPoint;
import com.commons.utilities.*;

public class ClientServer extends Thread {
	
	private ClientData clientData;
	private ServerSocket listener;
	private boolean hasBeenShutDown = false;
	public ClientServer(ClientData clientData) throws IOException {
		this.clientData = clientData;		
	}

	public void run() {
		try {
			int port = 5555;
			Console.show("Starting Server.");
			listener = new ServerSocket(port);
			Console.show("Server Listening at port: "+port);
			while (!hasBeenShutDown) {
				// New connection accepted
				Socket socket = listener.accept();
				try 
				{				
					// Start a new thread for client requests
					new Request(socket).start();
				}
				catch (Exception e) {
//					e.printStackTrace();
					listener.close();
				}
			}
		}
		catch(Exception ex) {
//			ex.printStackTrace();
			Console.show("Server client closed");
		}
	}
	
	public void shutdown() throws IOException {
		this.listener.close();
		this.hasBeenShutDown = true;
	}
	
	private class Request extends Thread {
		private IChannel channel;
		
		public Request(Socket socket) {
			this.channel = ChannelFactory.getChannel(ChannelFactory.channelType, socket, EndPoint.P2P_CLIENT);
		}
		
		public void run() {
			try {
				// Receive the response
				String input = new String(this.channel.receive(), "UTF-8");
				if(input.contains("GET:")) {
					Integer fileID = Integer.parseInt(input.split(":")[2]);
					String filePath = clientData.getFilesAndIds().get(fileID);
					if(filePath != null) {
						Console.show("Sending file " + filePath);
						byte[] file = FileUtils.getFile(filePath);
						this.channel.send(file);
						String logText = this.channel.getRemoteIP() + " - File request ID: "+fileID+" location "+filePath;
						FileUtils.writeTextToFile(Paths.sharedFiles +clientData.getIdentifier()+"/logs/files_sent", logText);
					}
					else {
						Console.show("FILE NOT FOUND!");
					}	
				}
				this.channel.close();
			}
			catch (Exception e) {				
				e.printStackTrace();
			}
		}
	}
}
