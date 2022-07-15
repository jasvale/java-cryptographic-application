package com.requests;

import com.channel.Channel;
import com.commons.enums.RequestType;

public class RequestFilesRequest extends GenericRequest {
    public RequestFilesRequest(Channel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_FILES;
    }
}
