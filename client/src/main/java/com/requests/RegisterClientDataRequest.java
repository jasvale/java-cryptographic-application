package com.requests;

import com.channel.Channel;
import com.commons.enums.RequestType;

public class RegisterClientDataRequest extends GenericRequest {

    public RegisterClientDataRequest(Channel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REGISTER_CLIENT_DATA;
    }
}
