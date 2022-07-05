package com.commons.channel;

import com.commons.cryptography.CipherType;
import com.commons.enums.EndPoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class UnsecuredChannel extends Channel {

    public UnsecuredChannel(Socket socket, EndPoint endPoint) {
        super(socket, endPoint);
    }

    public void send(byte[] bytes) throws Exception {
        DataOutputStream out = getDataOutputStream();
        flushOutMessageBytes(out, bytes);
    }

    public byte[] receive() throws Exception {
        DataInputStream in = getDataInputStream();
        return super.receiveMessageBytes(in);
    }

    @Override
    public void secureChanel(CipherType cipherType, byte[] macKey, byte[] cipherKey, byte[] IVKey) throws Exception {
        throw new Exception("Not possible in a unsecured channel.");
    }
}
