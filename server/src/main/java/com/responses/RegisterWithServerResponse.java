package com.responses;

import com.commons.enums.RequestType;
import com.server.RequestHandler;

public class RegisterWithServerResponse extends GenericResponse {

    public RegisterWithServerResponse(RequestHandler requestHandler) {
        super(requestHandler);
    }

    @Override
    public void send() throws Exception {
        String welcomeMessage = "Welcome, you are client #" + super.requestHandler.getClientNumber() + ".";
        super.channel.send(welcomeMessage.getBytes());
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.HELLO_SERVER;
    }
}
