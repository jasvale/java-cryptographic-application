package com.responses;

import com.commons.enums.RequestType;
import com.server.RequestHandler;

public class DisconnectFromServerResponse extends GenericResponse {

    public DisconnectFromServerResponse(RequestHandler requestHandler) {
        super(requestHandler);
    }

    @Override
    public void send() throws Exception {
        this.channel.send(("Good bye #"+super.requestHandler.getClientNumber()).getBytes());
        super.requestHandler.getClientManager().removeClient(super.requestHandler.getClientData());
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.BYE_SERVER;
    }
}
