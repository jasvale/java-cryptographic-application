package com.requests;

import com.channel.Channel;
import com.channel.ChannelContext;
import com.channel.ChannelType;
import com.commons.datastructures.ClientData;
import com.commons.enums.EndPoint;
import com.commons.enums.RequestType;
import com.commons.utilities.FileUtils;
import com.commons.utilities.Paths;

import java.io.FileOutputStream;
import java.net.Socket;
import java.util.UUID;

public class FetchClientFileRequest extends GenericRequest {

    private ClientData clientData;

    public FetchClientFileRequest(ClientData clientData) throws Exception {
        super();
        String[] command = super.clientInput.split(":");
        String P2P_IP = command[1];
        Socket p2pSocket = new Socket(P2P_IP, 5555);

        ChannelContext channelCtx = ChannelContext
                .builder()
                .endPoint(EndPoint.P2P_CLIENT)
                .channelType(ChannelType.UNSECURED)
                .socket(p2pSocket)
                .build();

        super.channel = Channel.builder().channelContext(channelCtx).build();
    }

    //Connect trough socket
    //Socket p2pSocket = new Socket(P2P_IP, 5555);
    //IChannel p2PChannel = ChannelFactory.getChannel(ChannelFactory.channelType, p2pSocket, RemoteEndPoint.P2P_CLIENT);
    //p2PChannel.send(input.getBytes());
    //byte[] file = p2PChannel.receive();
    //p2PChannel.close();
    //String fileName = UUID.randomUUID().toString();
    //try (FileOutputStream fos = new FileOutputStream(
    //        Paths.sharedFiles + this.clientData.getIdentifier()+"/downloaded/"+fileName
    //)) {
    //    fos.write(file);
    //}
    //String logText = "Downloaded file " + fileName + " from "+P2P_IP;
    //FileUtils.writeTextToFile(Paths.sharedFiles +this.clientData.getIdentifier()+"/logs/downloads", logText);

    @Override
    public byte[] receive() throws Exception {
        byte[] file = channel.receive();

        String fileName = UUID.randomUUID().toString();
        try (FileOutputStream fos = new FileOutputStream(
                Paths.sharedFiles + this.clientData.getIdentifier()+"/downloaded/"+fileName
        )) {
            fos.write(file);
        }
        String logText = "Downloaded file " + fileName;
        FileUtils.writeTextToFile(Paths.sharedFiles +this.clientData.getIdentifier()+"/logs/downloads", logText);

        channel.close();
        return "File received and stored at".getBytes();
    }


    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_CLIENT_FILE;
    }
}
