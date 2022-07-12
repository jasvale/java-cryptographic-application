package com.commons.channel;

import com.commons.cryptography.CipherType;
import com.commons.cryptography.GenericCipher;
import com.commons.cryptography.GenericCipherFactory;
import com.commons.generics.Triplet;
import com.commons.utilities.CipherUtils;
import com.commons.utilities.Utils;

import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SecuredChannelSharedKeys extends Channel {
    private GenericCipher genericCipher;
    private Key macKey;
    public SecuredChannelSharedKeys(
        ChannelContext channelCtx
    ) throws Exception {
        super(channelCtx);
        byte[] secretKey = CipherUtils.getSharedKey(channelCtx.cipherType());
        Triplet<byte[], byte[], byte[]> triplet = Utils.generateDerivedKeysFromSecretKey(secretKey);
        this.secureChanel(channelCtx.cipherType(), triplet.first, triplet.second, triplet.third);
    }

    public void secureChanel(CipherType cipherType, byte[] macKey, byte[] cipherKey, byte[] IVKey) throws Exception {
        this.genericCipher = GenericCipherFactory.getInstace(cipherType, cipherKey, IVKey);
        this.macKey = new SecretKeySpec(macKey, "AES");
    }

    public void send(byte[] bytes) throws Exception {
        DataOutputStream out = getDataOutputStream();
        byte[] ciphered = this.genericCipher.encipher(bytes);
        flushOutMessageBytes(out, ciphered);
        flushOutMessageBytes(out, this.getAuthenticationHash(ciphered));
    }

    public byte[] receive() throws Exception
    {
        DataInputStream in = getDataInputStream();
        byte[] received = super.receiveMessageBytes(in);
        byte[] receivedAuthenticationHash = super.receiveMessageBytes(in);
        byte[] digest = this.getAuthenticationHash(received);
        this.validateHash(digest, receivedAuthenticationHash);
        return this.genericCipher.decipher(received);
    }

    private void validateHash(byte[] localAuthenticationHash, byte[] remoteAuthenticationHash) throws Exception {
        String localHex = Utils.bytesToHex(localAuthenticationHash);
        String remoteHex = Utils.bytesToHex(remoteAuthenticationHash);
        if(!localHex.equals(remoteHex))
            throw new Exception("This message hash do not macth.");
    }

    private byte[] getAuthenticationHash(byte[] cipherBytes) throws InvalidKeyException, NoSuchAlgorithmException {
        return Utils.HMACSHA256(
                this.macKey,
                Utils.SHA256(
                        cipherBytes
                )
        );
    }
}
