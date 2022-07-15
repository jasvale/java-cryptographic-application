package com.requests;

import com.channel.Channel;
import com.commons.enums.RequestType;

public class DisconnectFromServerRequest extends GenericRequest {
    public DisconnectFromServerRequest(Channel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.BYE_SERVER;
    }
}
