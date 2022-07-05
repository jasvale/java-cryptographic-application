package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

public class DisconnectFromServerRequest extends GenericRequest {
    public DisconnectFromServerRequest(IChannel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.BYE_SERVER;
    }
}
