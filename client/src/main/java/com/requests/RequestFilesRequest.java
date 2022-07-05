package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

public class RequestFilesRequest extends GenericRequest {
    public RequestFilesRequest(IChannel channel) {
        super(channel);
    }

    @Override
    public RequestType getCommandType() {
        return RequestType.REQUEST_FILES;
    }
}
