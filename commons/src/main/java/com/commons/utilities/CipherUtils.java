package com.commons.utilities;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.commons.cryptography.GenericCipherType;

public class CipherUtils {
	private static final int keySize = 16;

	public static byte[] getSharedKey(GenericCipherType genericCipherType) throws IOException {
		return new ResourcesUtils().getFileFromResourceAsStream(
				Paths.sharedKeys + getFileNameForSharedKey(genericCipherType)
		).readAllBytes();
	}

	public static void createSharedKeys(GenericCipherType genericCipherType, String destinationFolder) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String fileName = getFileNameForSharedKey(genericCipherType);
		byte[] bytes =  Utils.getRandomKey(keySize);
		FileUtils.writeToFile(bytes, destinationFolder+"\\"+fileName);
	}

	public static String getFileNameForSharedKey(GenericCipherType genericCipherType) {
		return genericCipherType.toString() + "_key";
	}
}
