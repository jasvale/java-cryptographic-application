package com.responses;

import com.commons.enums.RequestType;
import com.server.RequestHandler;

public class RegisterClientDataResponse extends GenericResponse {

    public RegisterClientDataResponse(RequestHandler requestHandler) {
        super(requestHandler);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REGISTER_CLIENT_DATA;
    }
}
