package com.commons.channel;

import com.commons.cryptography.CipherType;
import com.commons.cryptography.GenericCipher;
import com.commons.cryptography.GenericCipherFactory;
import com.commons.diffieHellman.DHKeyAgreement;
import com.commons.diffieHellman.DHKeyAgreementInitiator;
import com.commons.diffieHellman.DHKeyAgreementReceiver;
import com.commons.enums.EndPoint;
import com.commons.generics.Triplet;
import com.commons.utilities.Utils;

import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
public class SecuredChannelDiffieHellman extends Channel {
    private GenericCipher genericCipher;
    private Key macKey;

    public SecuredChannelDiffieHellman(ChannelContext channelContext) throws Exception {
        super(channelContext);
        IChannel unsecuredChannel = ChannelFactory.getChannel(ChannelType.UNSECURED, socket, EndPoint.SERVER);

        DHKeyAgreement diffieHellman;
        if(channelContext.isRemoteAServer()) {
            diffieHellman = new DHKeyAgreementInitiator(unsecuredChannel);
        }
        else {
            diffieHellman = new DHKeyAgreementReceiver(unsecuredChannel);
        }

        Triplet<byte[], byte[], byte[]> derivedKeys = diffieHellman.exchangeAndDeriveKeys(
                channelContext.getPublicCertificate(), channelContext.getSignerCertificate(), channelContext.getPrivateKey()
        );

        this.secureChanel(channelContext.getCipherType(), derivedKeys.first, derivedKeys.second, derivedKeys.third);
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

    public byte[] receive() throws Exception {
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
