package com.requests;

import com.channel.Channel;
import com.commons.enums.RequestType;

public class GenericRequest implements Request {

    protected Channel channel;
    protected String clientInput;

    public GenericRequest() {}

    public GenericRequest(Channel channel) {
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
    public void configureClientInput(String input) {
        this.clientInput = clientInput;
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
