package com.channel.strategies.encryption;

import com.channel.ChannelContext;

public interface EncryptionStrategy {
    void init(ChannelContext channelCtx, byte[] cipherKey, byte[] IVKey) throws Exception;
    byte[] encipher(byte[] bytes) throws Exception;
    byte[] decipher(byte[] bytes) throws Exception;
}
