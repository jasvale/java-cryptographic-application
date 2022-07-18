package com.channel.strategies.signing;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SigningStrategy {
    byte[] HmacSha256(byte[] bytes, byte[] macKey) throws NoSuchAlgorithmException, InvalidKeyException;
}
