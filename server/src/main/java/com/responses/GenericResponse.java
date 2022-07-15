package com.responses;

import com.channel.Channel;
import com.commons.enums.RequestType;
import com.server.RequestHandler;

public class GenericResponse implements Response {

    protected RequestHandler requestHandler;
    protected Channel channel;

    public GenericResponse(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.channel = requestHandler.getChannel();
    }

    @Override
    public void send() throws Exception {

    }

    @Override
    public byte[] receive() throws Exception {
        return this.channel.receive();
    }

    @Override
    public RequestType getCommandType() {
        return null;
    }
}
