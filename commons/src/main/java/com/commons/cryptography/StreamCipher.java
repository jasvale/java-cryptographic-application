package com.commons.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class StreamCipher implements GenericCipher
{	
	private Cipher cipher;
	private Key key;
	
	public StreamCipher(byte[] key) throws Exception {
		this.initRC4(key);
	}
	
	@Override
	public byte[] encipher(byte[] bytes) throws InvalidKeyException, InvalidAlgorithmParameterException {
		this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
		return this.cipher.update(bytes);
	}
	
	@Override
	public byte[] decipher(byte[] bytes) throws InvalidKeyException, InvalidAlgorithmParameterException {
		this.cipher.init(Cipher.DECRYPT_MODE, this.key);		
		return this.cipher.update(bytes);
	}

	private void initRC4(byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("RC4");
		this.key = new SecretKeySpec(key, 0, 16, "RC4");
	}
	
}
