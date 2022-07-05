package com.commons.cryptography;

public class GenericCipherFactory {
	
	public static GenericCipher getInstace(CipherType cipherType, byte[] cipherKey, byte[] ivKey) throws Exception
	{
		switch (cipherType)
		{	
			case AES_CFB_NoPadding:
				return new BlockCipherNoPadding("AES/CFB/NoPadding", "AES", cipherKey, ivKey);
			case AES_CFB8_NoPadding:
				return new BlockCipherNoPadding("AES/CFB8/NoPadding", "AES", cipherKey, ivKey);
			case AES_CFB8_PKCS5Padding:
				return new BlockCipherPKCS5Padding("AES/CFB8/PKCS5Padding", "AES", cipherKey, ivKey);
			case AES_CBC_NoPadding:
				return new BlockCipherNoPadding("AES/CBC/NoPadding", "AES", cipherKey, ivKey);
			case AES_CBC_PKCS5Padding:
				new BlockCipherPKCS5Padding("AES/CBC/PKCS5Padding", "AES", cipherKey, ivKey);
			case RC4:
				return new StreamCipher(cipherKey);
			default:
				throw new Exception("Unknown Cryptography Algorithm");
		}
	}
	
}
