package com.commons.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class BlockCipherPKCS5Padding implements GenericCipher {
    private final Cipher cipher;
    private final Key secretKeySpec;
    private final AlgorithmParameterSpec ivSpec;

    public BlockCipherPKCS5Padding(String transformation, String algorithm, byte[] key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance(transformation);
        secretKeySpec = new SecretKeySpec(key, algorithm);
        ivSpec = new IvParameterSpec(iv);
    }

    @Override
    public byte[] encipher(byte[] bytes) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        return cipher.doFinal(bytes);
    }

    @Override
    public byte[] decipher(byte[] bytes) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        return cipher.doFinal(bytes);
    }
}
