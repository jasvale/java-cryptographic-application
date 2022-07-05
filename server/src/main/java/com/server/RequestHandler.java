package com.server;

import com.access_control.AccessControl;
import com.commons.channel.ChannelContext;
import com.commons.channel.ChannelFactory;
import com.commons.channel.ChannelType;
import com.commons.channel.IChannel;
import com.commons.enums.EndPoint;
import com.commons.enums.RequestType;
import com.commons.utilities.*;
import com.commons.cryptography.CipherType;
import com.commons.datastructures.ClientData;
import com.responses.Response;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

public class RequestHandler extends Thread {
    private final int clientNumber;
    private final ClientManager clientManager;
    private ClientData clientData;
    private final IChannel channel;

    public RequestHandler(
            Socket socket,
            int clientNumber,
            CipherType cipherType,
            ChannelType channelType,
            ClientManager clientManager,
            X509Certificate serverSubSaIssuer,
            X509Certificate serverPublicCert,
            RSAPrivateKey serverPrivateKey

    ) throws Exception {
        this.clientNumber = clientNumber;
        this.clientManager = clientManager;

        Console.show("new connection with client# " + clientNumber + " at " + socket);

        ChannelContext channelContext = new ChannelContext();
        channelContext.setCipherType(cipherType);
        channelContext.setChannelType(channelType);
        channelContext.setSocket(socket);
        channelContext.setRemoteEndPoint(EndPoint.CLIENT);
        channelContext.setPublicCertificate(serverPublicCert);
        channelContext.setSignerCertificate(serverSubSaIssuer);
        channelContext.setPrivateKey(serverPrivateKey);

        this.channel = ChannelFactory.getChannel(channelContext);
    }

    public void run() {
        try {
            Response.get(RequestType.HELLO_SERVER, this).send();

            this.clientData = clientManager.clientConnected(
                    Response.get(RequestType.REGISTER_CLIENT_DATA, this).receive(),
                    this.channel.getRemoteIP()
            );

            if(!isClientValidatedToAccessServer())
                return;

            while(true) {
                Response.get(RequestType.REQUEST_AVAILABLE_OPTIONS, this).send();
                Response response = null;
                boolean isValid = false;
                while(!isValid) {
                    String receivedInput = new String(this.channel.receive(), StandardCharsets.UTF_8).trim();
                    response = Response.get(receivedInput, this);
                    isValid = response != null;
                    if(!isValid) {
                        String invalidCommandError = "You are either hacking or buggy, goodbye.";
                        Console.show("#"+clientNumber+" - INVALID_INPUT["+receivedInput+"] - " + invalidCommandError);
                        this.channel.send(invalidCommandError.getBytes());
                        throw new Exception(invalidCommandError);
                    }
                }

                response.send();

                if(response.getCommandType().equals(RequestType.BYE_SERVER)) {
                    break;
                }
            }
            // Clean close, log information
            Console.show("#" + clientNumber + " closed.");
            clientManager.removeClient(this.clientData);
            // Close socket
            this.channel.close();
        }
        catch (EOFException e) {
            Console.show("#" + clientNumber + " closed the application.");
        }
        catch (Exception e) {
            Console.show("#" + clientNumber + " closed the application.");
			e.printStackTrace();
        }
    }

    private boolean isClientValidatedToAccessServer() throws IOException {
        if(!AccessControl.isUserValid(this.clientData)) {
            Console.show("#" + clientNumber + " - "+this.clientData.getIdentifier()+" closed.");
            clientManager.removeClient(this.clientData);
            this.channel.close();
            return false;
        }
        return true;
    }

    public int getClientNumber() {
        return this.clientNumber;
    }

    public ClientManager getClientManager() {
        return this.clientManager;
    }

    public ClientData getClientData() {
        return this.clientData;
    }

    public IChannel getChannel() {
        return this.channel;
    }

}
