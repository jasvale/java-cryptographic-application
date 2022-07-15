package com.channel;

import com.channel.strategies.encryption.EncryptionStrategy;
import com.channel.strategies.keyAgreement.KeyAgreementStrategy;
import com.channel.strategies.signing.SigningStrategy;
import com.commons.utilities.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BaseChannel implements Channel {

    protected ChannelContext channelCtx;
    protected int sequenceNumber = 0;

    protected KeyAgreementStrategy keyAgreementStrategy;
    protected EncryptionStrategy encryptionStrategy;
    protected SigningStrategy signingStrategy;

    public BaseChannel(ChannelContext channelCtx,
                       KeyAgreementStrategy keyAgreementStrategy,
                       EncryptionStrategy encryptionStrategy,
                       SigningStrategy signingStrategy) throws Exception {

        this.channelCtx = channelCtx;
        this.keyAgreementStrategy = keyAgreementStrategy;
        this.encryptionStrategy = encryptionStrategy;
        this.signingStrategy = signingStrategy;

        keyAgreementStrategy.agreeOnKeys(channelCtx);
        encryptionStrategy.init(
                channelCtx,
                keyAgreementStrategy.getCipherKey(),
                keyAgreementStrategy.getIvKey()
        );
    }

    @Override
    public ChannelContext getChannelContext() {
        return this.channelCtx;
    }

    @Override
    public void send(byte[] bytes) throws Exception {
        DataOutputStream out = getDataOutputStream();
        byte[] ciphered = encryptionStrategy.encipher(bytes);
        byte[] signature = signingStrategy.HmacSha256(ciphered, keyAgreementStrategy.getMacKey());
        send(out, ciphered);
        send(out, signature);
    }

    @Override
    public byte[] receive() throws Exception {
        DataInputStream in = getDataInputStream();
        byte[] received = receive(in);
        byte[] receivedSignature = receive(in);
        byte[] signature = signingStrategy.HmacSha256(received, keyAgreementStrategy.getMacKey());
        Utils.validateHash(signature, receivedSignature);
        return encryptionStrategy.decipher(received);
    }

    @Override
    public void close() throws IOException {
        this.channelCtx.socket().close();
    }

    private void send(DataOutputStream out, byte[] bytes) throws IOException {
        out.writeInt(bytes.length);
        out.write(bytes);
        out.flush();
    }

    private byte[] receive(DataInputStream in) throws IOException {
        int len = in.readInt();
        if(len == 0)
            return new byte[0];
        byte[] received = new byte[len];
        for(int i = 0; i < len; i++) {
            received[i] = in.readByte();
        }
        return received;
    }

    private DataOutputStream getDataOutputStream() throws IOException {
        DataOutputStream out = new DataOutputStream(this.channelCtx.socket().getOutputStream());
        this.sequenceNumber++;
        out.writeInt(this.sequenceNumber);
        return out;
    }

    private DataInputStream getDataInputStream() throws Exception {
        DataInputStream in = new DataInputStream(this.channelCtx.socket().getInputStream());
        this.sequenceNumber++;
        int receivedSequenceNumber = in.readInt();
        if(receivedSequenceNumber != this.sequenceNumber) {
            throw new Exception("Sequence numbers didn't match " + receivedSequenceNumber + " <> " + sequenceNumber);
        }
        return in;
    }
}
