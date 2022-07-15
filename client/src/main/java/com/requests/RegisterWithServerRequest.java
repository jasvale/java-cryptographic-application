package com.requests;

import com.channel.Channel;
import com.commons.enums.RequestType;

public class RegisterWithServerRequest extends GenericRequest {
    public RegisterWithServerRequest(Channel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.HELLO_SERVER;
    }
}
