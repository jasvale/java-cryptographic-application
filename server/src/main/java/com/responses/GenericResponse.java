package com.responses;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;
import com.commons.utilities.Console;
import com.server.RequestHandler;

public class GenericResponse implements Response {

    protected RequestHandler requestHandler;
    protected IChannel channel;

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
