package com.responses;

import com.commons.enums.RequestType;
import com.server.RequestHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface Response {

    List<String> allowedRequestsFromClients = Arrays.asList(
            RequestType.HELLO_SERVER.getInput(),
            RequestType.BYE_SERVER.getInput(),
            RequestType.REGISTER_CLIENT_DATA.getInput(),
            RequestType.REQUEST_FILES.getInput(),
            RequestType.REQUEST_AVAILABLE_OPTIONS.getInput()
    );

    void send() throws Exception;

    byte[] receive() throws Exception;

    RequestType getCommandType();

    static Response get(RequestType requestType, RequestHandler requestHandler) throws Exception {
        switch (requestType) {
            case HELLO_SERVER -> {
                return new RegisterWithServerResponse(requestHandler);
            }
            case REQUEST_AVAILABLE_OPTIONS -> {
                return new RequestAvailableOptionsResponse(requestHandler);
            }
            case REQUEST_FILES -> {
                return new RequestFilesResponse(requestHandler);
            }
            case BYE_SERVER -> {
                return new DisconnectFromServerResponse(requestHandler);
            }
            case REGISTER_CLIENT_DATA -> {
                return new RegisterClientDataResponse(requestHandler);
            }
            default -> throw new Exception("Could not find the ["+ RequestType.REGISTER_CLIENT_DATA +"] command.");
        }
    }

    static Response get(String receivedRequest, RequestHandler requestHandler) throws Exception {
        if(!allowedRequestsFromClients.contains(receivedRequest)) {
            return null;
        }

        Optional<RequestType> requestType = Arrays.stream(RequestType.values())
                .filter(e -> e.getInput().equals(receivedRequest))
                .findFirst();

        if(requestType.isEmpty())
            return null;

        return get(requestType.get(), requestHandler);
    }
}
