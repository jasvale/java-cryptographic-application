package com.requests;

import com.channel.Channel;
import com.commons.enums.RequestType;

public class RequestAvailableOptionsRequest extends GenericRequest {
    public RequestAvailableOptionsRequest(Channel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_AVAILABLE_OPTIONS;
    }
}
