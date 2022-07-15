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

public class BlockCipherNoPadding implements GenericCipher {
    private final Cipher cipher;
    private final Key secretKeySpec;
    private final AlgorithmParameterSpec ivSpec;
    private int blockSize;
    private int totalBlocks;
    private int end;

    public BlockCipherNoPadding(String transformation, String algorithm, byte[] key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance(transformation);
        secretKeySpec = new SecretKeySpec(key, algorithm);
        ivSpec = new IvParameterSpec(iv);
    }

    @Override
    public byte[] encipher(byte[] bytes) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        performBlocksCalculations(bytes.length);
        byte[] encipheredBytes = new byte[totalBlocks * blockSize];
        for(int i = 0, blockIndex = 1; i < end; i += blockSize, blockIndex++) {
            // extract a block
            byte[] block = new byte[blockSize];
            for(int k = 0; k < blockSize; k++) {
                block[k] = (k+i < bytes.length) ? bytes[k+i] : " ".getBytes()[0];
            }
            // cipher via update or doFinal
            byte[] ciphered;
            if(blockIndex < totalBlocks) {
                ciphered = cipher.update(block);
            }
            else {
                ciphered = cipher.doFinal(block);
            }
            // glue all ciphered blocks together
            for(int k = 0; k < blockSize; k++) {
                encipheredBytes[k+i] = ciphered[k];
            }
        }
        return encipheredBytes;
    }

    @Override
    public byte[] decipher(byte[] bytes) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        performBlocksCalculations(bytes.length);
        byte[] decipheredBytes = new byte[bytes.length];
        for(int i = 0, blockIndex = 1; i < end; i += blockSize, blockIndex++) {
            // extract a ciphered block
            byte[] ciphered = new byte[blockSize];
            for(int k = 0; k < blockSize; k++) {
                ciphered[k] =  bytes[k+i];
            }
            // decipher with update or doFinal
            byte[] deciphered;
            if(blockIndex < totalBlocks) {
                deciphered = cipher.update(ciphered);
            }
            else {
                deciphered = cipher.doFinal(ciphered);
            }
            // glue all deciphered bytes together
            for(int k = 0; k < blockSize; k++) {
                decipheredBytes[k+i] = deciphered[k];
            }
        }
        return decipheredBytes;
    }

    private void performBlocksCalculations(int receivedBytes) {
        int blockMultiplier = 1;
        blockSize = cipher.getBlockSize() * blockMultiplier;
        totalBlocks = receivedBytes / blockSize;
        int remainderOfTotalBlocksDivision = receivedBytes % blockSize;
        boolean needsExtraBlock = remainderOfTotalBlocksDivision > 0;
        if(needsExtraBlock) {
            totalBlocks++;
        }
        end = totalBlocks * blockSize;
    }
}
