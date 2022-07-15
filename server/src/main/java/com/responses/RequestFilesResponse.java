package com.responses;

import com.commons.enums.RequestType;
import com.server.RequestHandler;

public class RequestFilesResponse extends GenericResponse {

    public RequestFilesResponse(RequestHandler requestHandler) {
        super(requestHandler);
    }

    @Override
    public void send() throws Exception {
        this.channel.send(super.requestHandler.getClientManager().getFileList(super.requestHandler.getClientData()));
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_FILES;
    }
}
