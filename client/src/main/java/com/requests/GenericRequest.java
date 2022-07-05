package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

public class GenericRequest implements Request {

    protected IChannel channel;

    public GenericRequest(IChannel channel) {
        this.channel = channel;
    }

    @Override
    public void send() throws Exception {
        channel.send(getCommandType().getInput().getBytes());
    }

    @Override
    public void send(byte[] bytes) throws Exception {
       channel.send(bytes);
    }

    @Override
    public byte[] receive() throws Exception {
        return channel.receive();
    }

    @Override
    public RequestType getCommandType() {
        return null;
    }
}
