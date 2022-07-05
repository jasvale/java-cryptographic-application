package com.commons.utilities;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.commons.cryptography.CipherType;

public class CipherUtils {
	private static final int keySize = 16;

	public static byte[] getSharedKey(CipherType cipherType) throws IOException {
		return new ResourcesUtils().getFileFromResourceAsStream(
				Paths.sharedKeys + getFileNameForSharedKey(cipherType)
		).readAllBytes();
	}

	public static void createSharedKeys(CipherType cipherType, String destinationFolder) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String fileName = getFileNameForSharedKey(cipherType);
		byte[] bytes =  Utils.getRandomKey(keySize);
		FileUtils.writeToFile(bytes, destinationFolder+"\\"+fileName);
	}

	public static String getFileNameForSharedKey(CipherType cipherType) {
		return cipherType.toString() + "_key";
	}
}
