package com.commons.channel;

import com.commons.cryptography.CipherType;
import com.commons.enums.EndPoint;

import java.net.Socket;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

public record ChannelContext(
            ChannelType channelType,
            CipherType cipherType,
            Socket socket,
            EndPoint endPoint,
            X509Certificate publicCertificate,
            X509Certificate signerCertificate,
            RSAPrivateKey privateKey) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ChannelType channelType;
        private CipherType cipherType;
        private Socket socket;
        private EndPoint endPoint;
        private X509Certificate publicCertificate;
        private X509Certificate signerCertificate;
        private RSAPrivateKey privateKey;

        public ChannelContext build() {
            return new ChannelContext(
                    this.channelType,
                    this.cipherType,
                    this.socket,
                    this.endPoint,
                    this.publicCertificate,
                    this.signerCertificate,
                    this.privateKey
            );
        }

        public Builder channelType(ChannelType channelType) {
            this.channelType = channelType;
            return this;
        }

        public Builder cipherType(CipherType cipherType) {
            this.cipherType = cipherType;
            return this;
        }

        public Builder socket(Socket socket) {
            this.socket = socket;
            return this;
        }

        public Builder endPoint(EndPoint endPoint) {
            this.endPoint = endPoint;
            return this;
        }

        public Builder publicCertificate(X509Certificate publicCertificate) {
            this.publicCertificate = publicCertificate;
            return this;
        }

        public Builder signerCertificate(X509Certificate signerCertificate) {
            this.signerCertificate = signerCertificate;
            return this;
        }

        public Builder privateKey(RSAPrivateKey privateKey) {
            this.privateKey = privateKey;
            return this;
        }
    }
}
