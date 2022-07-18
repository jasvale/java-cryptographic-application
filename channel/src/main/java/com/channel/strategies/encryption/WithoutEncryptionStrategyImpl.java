package com.channel.strategies.encryption;

import com.channel.ChannelContext;
import com.commons.utilities.Console;

public class WithoutEncryptionStrategyImpl implements EncryptionStrategy {

    @Override
    public void init(ChannelContext channelCtx, byte[] cipherKey, byte[] IVKey) throws Exception {
        Console.show("No encryption strategy is configured.");
    }

    @Override
    public byte[] encipher(byte[] bytes) {
        return bytes;
    }

    @Override
    public byte[] decipher(byte[] bytes) {
        return bytes;
    }
}
