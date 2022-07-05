package com.commons.utilities;

import com.commons.generics.Triplet;

import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Mac;

public class Utils 
{	
	public static PublicKey getRSAPublicKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyFactory kf = KeyFactory.getInstance("RSA"); 		
		return kf.generatePublic(new X509EncodedKeySpec(bytes));
	}
	
	public static PrivateKey getRSAPrivateKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyFactory kf = KeyFactory.getInstance("RSA"); 		
		return kf.generatePrivate(new PKCS8EncodedKeySpec(bytes));
	}
	
	public static byte[] sign(RSAPrivateKey rsaPrivateKey, byte[] bytes)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature signature = Signature.getInstance("SHA256withRSA");
	    signature.initSign(rsaPrivateKey);
	    signature.update(bytes);
	    return signature.sign();
	}
	
	public static boolean verify(X509Certificate x509Certificate, byte[] bytes, byte[] signature)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(x509Certificate.getPublicKey());
        sig.update(bytes);
        return sig.verify(signature);
	}	
	
	public static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder();
	    for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) {
				hexString.append('0');
			}
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	public static byte[] SHA256(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bytes);
		return md.digest();
	}

	public static byte[] SHA256(byte[] bytes, int key) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bytes);
		md.update((byte)key);
		return md.digest();
	}
	
	public static byte[] HMACSHA256(Key key, byte[] bytes) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
		return mac.doFinal(bytes);
	}
	
	public static byte[] getRandomKey(int size) throws NoSuchAlgorithmException, NoSuchProviderException {
		//SecureRandom secureRandom = SecureRandom.getInstance("NativePRNGBlocking");
		//SecureRandom secureRandom = SecureRandom.getInstance("NativePRNGNonBlocking");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
		byte[] bytes = new byte[size];
		secureRandom.nextBytes(bytes);
		return bytes;
	}

	public static Triplet<byte[], byte[], byte[]> generateDerivedKeysFromSecretKey(byte[] secretKey) throws NoSuchAlgorithmException {
		byte[] macKey = Utils.SHA256(secretKey, 1);
		macKey = Arrays.copyOfRange(macKey, 0, 16);
		byte[] cipherKey = Utils.SHA256(secretKey, 2);
		cipherKey = Arrays.copyOfRange(cipherKey, 0, 16);
		byte[] IVKey = Utils.SHA256(secretKey, 3);
		IVKey = Arrays.copyOfRange(IVKey, 0, 16);
		Console.show("> derived keys from master key");
		Console.show(">	    mac key    : " + Utils.bytesToHex(macKey));
		Console.show(">	    cipher key : " + Utils.bytesToHex(cipherKey));
		Console.show(">	    iv key     : " + Utils.bytesToHex(IVKey));
		return Triplet.of(macKey, cipherKey, IVKey);
	}
}
