package com.server;

import com.access_control.AccessControl;
import com.channel.ChannelContext;
import com.channel.ChannelType;
import com.channel.Channel;
import com.commons.enums.EndPoint;
import com.commons.enums.RequestType;
import com.commons.utilities.*;
import com.commons.cryptography.GenericCipherType;
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
    private final Channel channel;

    public RequestHandler(
            Socket socket,
            int clientNumber,
            GenericCipherType genericCipherType,
            ChannelType channelType,
            ClientManager clientManager,
            X509Certificate serverSubSaIssuer,
            X509Certificate serverPublicCert,
            RSAPrivateKey serverPrivateKey

    ) throws Exception {
        this.clientNumber = clientNumber;
        this.clientManager = clientManager;

        Console.show("new connection with client# " + clientNumber + " at " + socket);

        ChannelContext channelCtx = ChannelContext.builder()
                .cipherType(genericCipherType)
                .channelType(channelType)
                .socket(socket)
                .endPoint(EndPoint.CLIENT)
                .publicCertificate(serverPublicCert)
                .signerCertificate(serverSubSaIssuer)
                .privateKey(serverPrivateKey)
                .build();

        this.channel = Channel.builder().channelContext(channelCtx).build();
    }

    public void run() {
        try {
            Response.get(RequestType.HELLO_SERVER, this).send();

            this.clientData = clientManager.clientConnected(
                    Response.get(RequestType.REGISTER_CLIENT_DATA, this).receive(),
                    this.channel.getChannelContext().getRemoteIp()
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
            Console.show("#" + clientNumber + " closed.");
            clientManager.removeClient(this.clientData);
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

    public Channel getChannel() {
        return this.channel;
    }

}
