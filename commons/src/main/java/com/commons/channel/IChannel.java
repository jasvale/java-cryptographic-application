package com.commons.channel;

import com.commons.cryptography.CipherType;
import com.commons.enums.EndPoint;

import java.io.IOException;

public interface IChannel {
    void send(byte[] bytes) throws Exception;
    byte[] receive() throws Exception;
    void close() throws IOException;
    void secureChanel(CipherType cipherType, byte[] macKey, byte[] cipherKey, byte[] IVKey) throws Exception;
    String getRemoteIP();
    EndPoint getRemoteEndPoint();
}
