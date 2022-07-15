package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

import com.commons.utilities.CERTUtils;
import com.commons.utilities.Console;
import com.commons.utilities.Paths;
import com.configuration.GlobalConfig;

public class Server {

	public static void main(String[] args) throws Exception {
		new Server();
	}

	private X509Certificate serverCertIssuer;
	private X509Certificate serverPublicCert;
	private RSAPrivateKey serverPrivateKey;

	public Server() throws GeneralSecurityException, IOException {
		ClientManager clientManager = new ClientManager();
		loadServerCertificates();
		loadServerPrivateKey();

		try (ServerSocket listener = startUpServerSocket()) {
			int clientNumber = 0;
			while (true) {
				Socket socket = listener.accept();
				try {
					new RequestHandler(
							socket,
							clientNumber++,
							GlobalConfig.getInstance().getCipherType(),
							GlobalConfig.getInstance().getChannelType(),
							clientManager,
							serverCertIssuer,
							serverPublicCert,
							serverPrivateKey
					).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private ServerSocket startUpServerSocket() throws IOException {
		int port = 4444;
		Console.show("starting Server.");
		ServerSocket listener = new ServerSocket(port);
		Console.show("server listening at port: "+port);
		return listener;
	}

	private void loadServerCertificates() throws GeneralSecurityException, IOException {
		this.serverPublicCert = CERTUtils.loadPublicX509(Paths.certs +"server.pem");
		this.serverCertIssuer = CERTUtils.loadPublicX509(Paths.certsRootCa +"rootca.pem");
		Console.showCertificateIssuerAndCN("my certificate issuer", serverCertIssuer);
		Console.showCertificateIssuerAndCN("my certificate", serverPublicCert);
	}

	private void loadServerPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		this.serverPrivateKey = CERTUtils.loadRSAPrivateKey(Paths.certs +"server_pK.pkcs8");
	}
}
