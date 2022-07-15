package com.channel.strategies.signing;

import com.commons.utilities.Utils;

import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class WithSigningStrategyImpl implements SigningStrategy {

    @Override
    public byte[] HmacSha256(byte[] bytes, byte[] macKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Key kMacKey = new SecretKeySpec(macKey, "AES");
        return Utils.HMACSHA256(
                kMacKey,
                Utils.SHA256(
                        bytes
                )
        );
    }
}
