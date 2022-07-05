package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

public class FetchClientFileRequest extends GenericRequest {
    public FetchClientFileRequest(IChannel channel) {
        super(channel);
    }

    @Override
    public void send(byte[] bytes) throws Exception {
        /*
        String[] command = input.split(":");
        String P2P_IP = command[1];

        // Connect trough socket
        Socket p2pSocket = new Socket(P2P_IP, 5555);
        IChannel p2PChannel = ChannelFactory.getChannel(ChannelFactory.channelType, p2pSocket, RemoteEndPoint.P2P_CLIENT);
        p2PChannel.send(input.getBytes());
        byte[] file = p2PChannel.receive();
        p2PChannel.close();
        String fileName = UUID.randomUUID().toString();
        try (FileOutputStream fos = new FileOutputStream(
                Paths.sharedFiles + this.clientData.getIdentifier()+"/downloaded/"+fileName
        )) {
            fos.write(file);
        }
        String logText = "Downloaded file " + fileName + " from "+P2P_IP;
        FileUtils.writeTextToFile(Paths.sharedFiles +this.clientData.getIdentifier()+"/logs/downloads", logText);
         */
    }

    @Override
    public byte[] receive() throws Exception {
        return channel.receive();
    }


    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_CLIENT_FILE;
    }
}
