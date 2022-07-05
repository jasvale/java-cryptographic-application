package com.commons.cryptography;

public interface GenericCipher
{
	byte[] encipher(byte[] bytes) throws Exception;
	byte[] decipher(byte[] bytes) throws Exception;
}
