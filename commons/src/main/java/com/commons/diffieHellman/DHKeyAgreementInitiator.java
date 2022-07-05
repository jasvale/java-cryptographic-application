package com.commons.diffieHellman;

import com.commons.channel.IChannel;
import com.commons.generics.Triplet;
import com.commons.utilities.Console;
import com.commons.utilities.Utils;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class DHKeyAgreementInitiator extends DHKeyAgreement {

    public DHKeyAgreementInitiator(IChannel channel) throws NoSuchAlgorithmException {
        super.channel = channel;
    }

    @Override
    public Triplet<byte[], byte[], byte[]> exchangeAndDeriveKeys(
            X509Certificate publicCertificate, X509Certificate subSaIssuerCertificate, RSAPrivateKey privateKey
    ) throws Exception {
        Console.show("initiating an Diffie-Hellman key agreement.");

        generateKeyPair();
        Console.show("generated Diffie-Hellman key pair.");

        generateKeyAgreement();
        Console.show("created the java protocol class for Diffie-Hellman key exchange.");

        sendDHPublicKey();
        Console.show("sent my Diffie-Hellman public parameter.");

        receiveDHPublicKey();
        Console.show("received remote Diffie-Hellman public parameter.");

        generateSecretKeyFromDHAgreement();
        Console.show("generated master secret key from exchange: " + Utils.bytesToHex(Utils.SHA256(secretKey)));

        send(publicCertificate);
        Console.showCertificateIssuerAndCN("sent my public certificate", publicCertificate);

        X509Certificate remotePublicCertificate = receiveX509Certificate();
        Console.showCertificateIssuerAndCN("received remote public certificate", remotePublicCertificate);

        send(subSaIssuerCertificate);
        Console.showCertificateIssuerAndCN("sent my certificate signer (issuer) certificate", subSaIssuerCertificate);

        X509Certificate remotePublicSubSaCertificate = receiveX509Certificate();
        Console.showCertificateIssuerAndCN("received remote certificate signer (issuer) certificate", remotePublicSubSaCertificate);

        addSubCAIfTrusted(remotePublicSubSaCertificate);
        Console.show("received certificate authority/sub-authority is valid, and as been added to the list.");

        validateCertificate(remotePublicCertificate);
        Console.show("remote public certificate as been validated.");

        byte[] mergedOrdered = getMergedOrderedBytes();
        byte[] signed = Utils.sign(privateKey, mergedOrdered);

        this.channel.send(signed);
        Console.show("sts protocol: sent my signature of the merged received key + my public key.");

        byte[] signedReceived = this.channel.receive();
        Console.show("sts protocol: received remote signature.");

        Console.showStationToStationLogs(mergedOrdered, signed, signedReceived);

        validateSignatures(remotePublicCertificate, mergedOrdered, signedReceived);

        return Utils.generateDerivedKeysFromSecretKey(secretKey);
    }

    @Override
    protected void generateKeyPair() {
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    @Override
    protected void generateKeyAgreement() throws NoSuchAlgorithmException, InvalidKeyException {
        keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(super.keyPair.getPrivate());
    }

    @Override
    public byte[] getMergedOrderedBytes() {
        byte[] merged = new byte[publicKey.length + receivedKey.length];
        System.arraycopy(receivedKey,0,merged,0 , receivedKey.length);
        System.arraycopy(publicKey,0,merged, receivedKey.length, publicKey.length);
        return merged;
    }
}
