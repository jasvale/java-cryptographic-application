package com.channel.strategies.signing;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class WithoutSigningStrategyImpl implements SigningStrategy {

    @Override
    public byte[] HmacSha256(byte[] bytes, byte[] macKey) throws NoSuchAlgorithmException, InvalidKeyException {
        return new byte[0];
    }
}
