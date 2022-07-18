package com.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

import com.channel.ChannelConfig;
import com.channel.ChannelContext;
import com.requests.Request;
import com.commons.enums.RequestType;
import com.channel.Channel;
import com.commons.enums.EndPoint;
import com.commons.datastructures.ClientData;
import com.commons.utilities.*;

public class Client {
	public static void main(String[] args) throws Exception {
		new Client().connect();
	}

	private Channel channel;
	private ClientData clientData;
	private ClientServer clientServer;
	private final Scanner scanner = new Scanner(System.in);
	private String username;
	private Socket serverSocket;

	public Client() throws Exception {
		this.askAndReadUsernameFromInput();
		this.createFoldersStructure();
		this.loadClientData();
		this.startupClientServer();
	}

	public void connect() throws Exception {
		try {
			connectToServer();

			Console.show(
					channel.getChannelContext().endPoint(),
					Request.get(RequestType.HELLO_SERVER, channel, clientData).receive()
			);

			Request.get(RequestType.REGISTER_CLIENT_DATA, channel, clientData).send(clientData.getData());

			while(true) {
				Console.show(
						channel.getChannelContext().endPoint(),
						Request.get(RequestType.REQUEST_AVAILABLE_OPTIONS, channel, clientData).receive()
				);

				Request request = null;
				boolean isValid = false;
				while(!isValid) {
					Console.input("please select one option");
					String input = scanner.nextLine();
					request = Request.get(input, channel, clientData);
					isValid = request != null;
					if(!isValid) {
						Console.show("Invalid command, please enter a valid one or 'exit' to leave.");
						continue;
					}
					request.configureClientInput(input);
				}
				request.send();
				Console.show(channel.getChannelContext().endPoint(), request.receive());
				if(request.getCommandType().equals(RequestType.BYE_SERVER))
					break;
			}
		}
		catch (ConnectException e) {
			clientServer.shutdown();
			Console.show("Server is not turned off or not accepting connections.");
		}
		catch (Exception e) {
			e.printStackTrace();
			serverSocket.close();
			clientServer.shutdown();
		}
	}

	private void createFoldersStructure() throws Exception {
		FileUtils.newFolder(Paths.sharedFiles);
		FileUtils.newFolder(Paths.sharedFiles + username);
		FileUtils.newFolder(Paths.sharedFiles + username + "/downloaded");
		FileUtils.newFolder(Paths.sharedFiles + username + "/logs");
	}

	private void loadClientData() throws IOException, URISyntaxException {
		this.clientData = new ClientData(username);
	}

	private void startupClientServer() throws IOException {
		this.clientServer = new ClientServer(this.clientData);
		this.clientServer.start();
	}

	private boolean isUsernameValid(String username) {
		return !Objects.isNull(username) && !username.isBlank();
	}

	private void askAndReadUsernameFromInput() throws Exception {
		Console.show("Who are you?");

		int tries = 0, maxTries = 4;
		String usernameTmp = "";
		boolean isValid = false;

		while(tries < maxTries && !isValid) {
			usernameTmp = scanner.nextLine();
			isValid = isUsernameValid(usernameTmp);
			tries++;
		}

		if(!isValid)
			throw new Exception("Please input a valid username");

		this.username = usernameTmp;
	}

	private void connectToServer() throws Exception {
		String serverAddress = "127.0.0.1";
		serverSocket = new Socket(serverAddress, 4444);

		ChannelContext channelCtx = ChannelContext
				.builder()
				.channelType(ChannelConfig.getInstance().getChannelType())
				.cipherType(ChannelConfig.getInstance().getCipherType())
				.socket(serverSocket)
				.endPoint(EndPoint.SERVER)
				.publicCertificate(CERTUtils.loadPublicX509(Paths.certs +"client.pem"))
				.signerCertificate(CERTUtils.loadPublicX509(Paths.certsIntermediate +"intermediate.pem"))
				.privateKey(CERTUtils.loadRSAPrivateKey(Paths.certs +"client_pK.pkcs8"))
				.build();

		this.channel = Channel.builder().channelContext(channelCtx).build();
	}
}
