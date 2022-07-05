package com.requests;

import com.commons.channel.IChannel;
import com.commons.enums.RequestType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface Request {

    List<String> allowedInputsFromConsole = Arrays.asList(
            RequestType.BYE_SERVER.getInput(),
            RequestType.REQUEST_FILES.getInput(),
            RequestType.REQUEST_AVAILABLE_OPTIONS.getInput()
    );

    void send() throws Exception;
    void send(byte[] bytes) throws Exception;
    byte[] receive() throws Exception;
    RequestType getCommandType();

    static Request get(RequestType requestType, IChannel channel) throws Exception {
        switch (requestType) {
            case HELLO_SERVER -> {
                return new RegisterWithServerRequest(channel);
            }
            case REQUEST_FILES -> {
                return new RequestFilesRequest(channel);
            }
            case REQUEST_CLIENT_FILE -> {
                return new FetchClientFileRequest(channel);
            }
            case BYE_SERVER -> {
                return new DisconnectFromServerRequest(channel);
            }
            case REGISTER_CLIENT_DATA -> {
                return new RegisterClientDataRequest(channel);
            }
            case REQUEST_AVAILABLE_OPTIONS -> {
                return new RequestAvailableOptionsRequest(channel);
            }
            default -> throw new Exception("Could not find the ["+ requestType +"] command.");
        }
    }

    static Request get(String consoleInput, IChannel channel) throws Exception {
        if(!allowedInputsFromConsole.contains((consoleInput)))
            return null;

        Optional<RequestType> command = Arrays.stream(RequestType.values())
                .filter(e -> e.getInput().equals(consoleInput))
                .findFirst();

        if(command.isEmpty())
            return null;

        return get(command.get(), channel);
    }
}
