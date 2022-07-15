package com.requests;

import com.channel.Channel;
import com.commons.datastructures.ClientData;
import com.commons.enums.RequestType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface Request {

    List<String> allowedInputsFromConsole = Arrays.asList(
            RequestType.BYE_SERVER.getInput(),
            RequestType.REQUEST_FILES.getInput(),
            RequestType.REQUEST_AVAILABLE_OPTIONS.getInput(),
            RequestType.REQUEST_CLIENT_FILE.getInput()
    );

    void send() throws Exception;
    void send(byte[] bytes) throws Exception;
    void configureClientInput(String input);
    byte[] receive() throws Exception;
    RequestType getCommandType();

    static Request get(RequestType requestType, Channel channel, ClientData clientData) throws Exception {
        switch (requestType) {
            case HELLO_SERVER -> {
                return new RegisterWithServerRequest(channel);
            }
            case REQUEST_FILES -> {
                return new RequestFilesRequest(channel);
            }
            case REQUEST_CLIENT_FILE -> {
                return new FetchClientFileRequest(clientData);
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

    static Request get(String consoleInput, Channel channel, ClientData clientData) throws Exception {
        if(!allowedInputsFromConsole.contains((consoleInput)) && !consoleInput.startsWith(RequestType.REQUEST_CLIENT_FILE.getInput()))
            return null;

        Optional<RequestType> command = Arrays.stream(RequestType.values())
                .filter(e -> (e.getInput().equals(consoleInput) || consoleInput.startsWith(RequestType.REQUEST_CLIENT_FILE.getInput())))
                .findFirst();

        if(command.isEmpty())
            return null;

        return get(command.get(), channel, clientData);
    }
}
