package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

public class RegisterWithServerRequest extends GenericRequest {
    public RegisterWithServerRequest(IChannel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.HELLO_SERVER;
    }
}
