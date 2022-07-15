package com.commons.enums;

public enum RequestType {
    HELLO_SERVER("888"),
    REGISTER_CLIENT_DATA("999"),
    REQUEST_AVAILABLE_OPTIONS("777"),
    REQUEST_FILES("1"),
    REQUEST_CLIENT_FILE("GET"),
    BYE_SERVER("exit");

    private final String input;
    RequestType(String input) {
        this.input = input;
    }
    public String getInput() {
        return input;
    }
}
