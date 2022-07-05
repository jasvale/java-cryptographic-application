package com.commons.channel;


import com.commons.cryptography.CipherType;
import com.commons.enums.EndPoint;

import java.net.Socket;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

public class ChannelContext {
    private ChannelType channelType;
    private CipherType cipherType;
    private Socket socket;
    private EndPoint endPoint;
    private X509Certificate publicCertificate;
    private X509Certificate signerCertificate;
    private RSAPrivateKey privateKey;

    public boolean isRemoteAServer() {
        return this.endPoint.equals(EndPoint.SERVER);
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public CipherType getCipherType() {
        return cipherType;
    }

    public void setCipherType(CipherType cipherType) {
        this.cipherType = cipherType;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public EndPoint getRemoteEndPoint() {
        return endPoint;
    }

    public void setRemoteEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public X509Certificate getPublicCertificate() {
        return publicCertificate;
    }

    public void setPublicCertificate(X509Certificate publicCertificate) {
        this.publicCertificate = publicCertificate;
    }

    public X509Certificate getSignerCertificate() {
        return signerCertificate;
    }

    public void setSignerCertificate(X509Certificate signerCertificate) {
        this.signerCertificate = signerCertificate;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
