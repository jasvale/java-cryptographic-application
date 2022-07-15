package com.channel.strategies.encryption;

import com.channel.ChannelContext;
import com.commons.cryptography.GenericCipher;
import com.commons.cryptography.GenericCipherFactory;

public class WithEncryptionStrategyImpl implements EncryptionStrategy {

    private GenericCipher genericCipher;

    @Override
    public void init(ChannelContext channelCtx, byte[] cipherKey, byte[] iVKey) throws Exception {
        genericCipher = GenericCipherFactory.getInstance(channelCtx.genericCipherType(), cipherKey, iVKey);
    }

    @Override
    public byte[] encipher(byte[] bytes) throws Exception {
        return genericCipher.encipher(bytes);
    }

    @Override
    public byte[] decipher(byte[] bytes) throws Exception {
        return genericCipher.decipher(bytes);
    }
}
