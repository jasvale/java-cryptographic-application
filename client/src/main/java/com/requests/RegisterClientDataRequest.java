package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

public class RegisterClientDataRequest extends GenericRequest {

    public RegisterClientDataRequest(IChannel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REGISTER_CLIENT_DATA;
    }
}
