package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

public class RequestAvailableOptionsRequest extends GenericRequest {
    public RequestAvailableOptionsRequest(IChannel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_AVAILABLE_OPTIONS;
    }
}
