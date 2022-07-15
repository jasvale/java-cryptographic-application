package com.commons.crytography;

import com.commons.cryptography.*;
import com.commons.utilities.Utils;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GenericCipherTest {

    private final String text;

    public GenericCipherTest() {
        text = "The quick brown fox jumps over the lazy dog.";
    }

    @Test
    public void AES_CBC_NoPadding_PlainJava_encipher_decipher_test() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        Key secretKeySpec = new SecretKeySpec(Utils.getRandomKey(16), "AES");
        AlgorithmParameterSpec iv = new IvParameterSpec(Utils.getRandomKey(16));
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

        int blockMultiplier = 1;
        byte[] bytes = text.getBytes();
        int totalBytes = bytes.length;
        System.out.println("totalBytes: " + totalBytes);
        int blockSize = cipher.getBlockSize() * blockMultiplier;
        System.out.println("blockSize: " + blockSize);
        int totalBlocks = totalBytes / blockSize;
        System.out.println("totalBlocks: " + totalBlocks);
        int remainderOfTotalBlocksDivision = totalBytes % blockSize;
        System.out.println("remainderOfTotalBlocksDivision: " + remainderOfTotalBlocksDivision);
        boolean needsExtraBlock = remainderOfTotalBlocksDivision > 0;
        if(needsExtraBlock) {
            totalBlocks++;
        }
        System.out.println("final totalBlocks: " + totalBlocks);

        int start = 0, end = totalBlocks * blockSize;
        System.out.println("start " + start + " end " + end);

        Map<Integer, byte[]> blocks = new LinkedHashMap<>();
        for(int i = 0, blockIndex = 1; i < end; i += blockSize, blockIndex++) {
            byte[] block = new byte[blockSize];
            for(int k = 0; k < blockSize; k++) {
                block[k] = (k+i < bytes.length) ? bytes[k+i] : " ".getBytes()[0];
            }
            byte[] ciphered;
            if(blockIndex < totalBlocks) {
                ciphered = cipher.update(block);
            }
            else {
                ciphered = cipher.doFinal(block);
            }
            blocks.put(blockIndex, ciphered);
        }
        blocks.forEach((key, value) -> System.out.println("Block " + key + " Ciphered " + Utils.bytesToHex(value)));

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        byte[] decipheredBytes = new byte[totalBlocks * blockSize];
        int blockIndex = 1;
        for(int i = 0; i < end; i += blockSize, blockIndex++) {
            byte[] ciphered = blocks.get(blockIndex);
            byte[] deciphered;
            if(blockIndex < totalBlocks) {
                deciphered = cipher.update(ciphered);
            }
            else {
                deciphered = cipher.doFinal(ciphered);
            }

            System.arraycopy(deciphered, 0, decipheredBytes, i, blockSize);
        }
        String decipheredString = new String(decipheredBytes, StandardCharsets.UTF_8).trim();
        System.out.println("Final Result: " + decipheredString);
        assertEquals(
                text.length(),
                decipheredString.length()
        );
        assertEquals(
               text,
                decipheredString
        );
    }


    @Test
    public void BlockCipherNoPadding_AES_CBC_Encipher_Then_Decipher() throws Exception {
        this.assertByCipherType(GenericCipherType.AES_CBC_NoPadding);
    }

    @Test
    public void BlockCipherNoPadding_AES_CFB8_Encipher_Then_Decipher() throws Exception {
        this.assertByCipherType(GenericCipherType.AES_CFB8_NoPadding);
    }

    @Test
    public void BlockCipherNoPadding_AES_CFB_Encipher_Then_Decipher() throws Exception {
        this.assertByCipherType(GenericCipherType.AES_CFB_NoPadding);
    }

    @Test
    public void BlockCipherPKCS5Padding_AES_CBC_Encipher_Then_Decipher() throws Exception {
        this.assertByCipherType(GenericCipherType.AES_CBC_PKCS5Padding);
    }

    @Test
    public void BlockCipherPKCS5Padding_AES_CFB8_Encipher_Then_Decipher() throws Exception {
        this.assertByCipherType(GenericCipherType.AES_CFB8_PKCS5Padding);
    }

    private void assertByCipherType(GenericCipherType genericCipherType) throws Exception {
        GenericCipher genericCipher = GenericCipherFactory.getInstance(
                genericCipherType,
                Utils.getRandomKey(16),
                Utils.getRandomKey(16)
        );

        String decipheredString = new String(genericCipher.decipher(genericCipher.encipher(text.getBytes())), StandardCharsets.UTF_8).trim();
        System.out.println("Final Result: " + decipheredString);
        assertEquals(
                text.length(),
                decipheredString.length()
        );
        assertEquals(
                text,
                decipheredString
        );
    }
}
