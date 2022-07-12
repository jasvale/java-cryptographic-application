package com.commons.diffieHellman;

import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.*;

import javax.crypto.*;

import com.commons.channel.IChannel;
import com.commons.generics.Triplet;
import com.commons.utilities.CERTUtils;
import com.commons.utilities.Console;
import com.commons.utilities.Utils;

public abstract class DHKeyAgreement {
	protected KeyFactory keyFactory = KeyFactory.getInstance("DH");
	protected KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");;
	protected KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
	protected KeyPair keyPair = null;

	protected IChannel channel;
	protected byte[] publicKey;
	protected byte[] receivedKey;
	protected byte[] secretKey = null;

	protected DHKeyAgreement() throws NoSuchAlgorithmException {

	}

	public abstract Triplet<byte[], byte[], byte[]> exchangeAndDeriveKeys(
			X509Certificate publicCertificate, X509Certificate subSaIssuerCertificate, RSAPrivateKey privateKey
	) throws Exception;

	public abstract byte[] getMergedOrderedBytes();

	protected void send(X509Certificate certificate) throws Exception {
		this.channel.send(certificate.getEncoded());
	}

	protected X509Certificate receiveX509Certificate() throws Exception {
		return CERTUtils.getPublicX509FromSocket(this.channel.receive());
	}

	protected abstract void generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException;

	protected abstract void generateKeyAgreement() throws NoSuchAlgorithmException, InvalidKeyException;
	
	protected void sendDHPublicKey() throws Exception {
		this.publicKey = this.keyPair.getPublic().getEncoded();
        this.channel.send(this.publicKey);
	}
	
	protected void receiveDHPublicKey() throws Exception {
		this.receivedKey = this.channel.receive();
	}

	protected void generateSecretKeyFromDHAgreement() throws InvalidKeySpecException, InvalidKeyException {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(receivedKey);
		PublicKey receivedPublicKey = keyFactory.generatePublic(x509KeySpec);
		keyAgreement.doPhase(receivedPublicKey, true);
		this.secretKey = keyAgreement.generateSecret();
	}

	protected void addSubCAIfTrusted(X509Certificate certSubSa) throws Exception {
		if(!CERTUtils.addSubCAIfTrusted(certSubSa)) {
			throw new Exception("Sub SA Certificate not trusted!");
		}
	}

	protected void validateCertificate(X509Certificate certificate) throws Exception {
		if(!CERTUtils.isTrusted(certificate)) {
			throw new Exception("Certificate not trusted! Issuer origin not found.");
		}
	}

	protected void validateSignatures(X509Certificate publicCert, byte[] mergedOrdered, byte[] signedReceived)
			throws Exception {
		if(Utils.verify(publicCert, mergedOrdered, signedReceived)) {
			Console.show("the station-to-station signature checks.");
		}
		else {
			throw new Exception("Exchange went wrong! received and sent are not the same. Signature wrong");
		}
	}

}
