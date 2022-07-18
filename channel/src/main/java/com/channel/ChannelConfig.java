package com.channel;

import com.commons.cryptography.GenericCipherType;

public class ChannelConfig {

    private final ChannelType channelType = ChannelType.SECURED_DIFFIE_HELLMAN;
    private final GenericCipherType genericCipherType = GenericCipherType.AES_CBC_NoPadding;

    public ChannelType getChannelType() {
        return channelType;
    }

    public GenericCipherType getCipherType() {
        return genericCipherType;
    }

    private static volatile ChannelConfig instance = null;

    private ChannelConfig() {}

    public static ChannelConfig getInstance() {
        if (instance == null) {
            synchronized(ChannelConfig.class) {
                if (instance == null) {
                    instance = new ChannelConfig();
                }
            }
        }
        return instance;
    }
}
