package com.responses;

import com.commons.enums.RequestType;
import com.commons.utilities.Console;
import com.server.RequestHandler;

public class RequestAvailableOptionsResponse  extends GenericResponse {

    public RequestAvailableOptionsResponse(RequestHandler requestHandler) {
        super(requestHandler);
    }

    @Override
    public void send() throws Exception {
        String actionRequest = "Available Options:\n  1 - List available files with IPs";
        this.channel.send(actionRequest.getBytes());
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_AVAILABLE_OPTIONS;
    }
}