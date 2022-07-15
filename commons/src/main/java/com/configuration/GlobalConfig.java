package com.configuration;

import com.channel.ChannelType;
import com.commons.cryptography.GenericCipherType;

public class GlobalConfig {

    private final ChannelType channelType = ChannelType.SECURED_DIFFIE_HELLMAN;
    private final GenericCipherType genericCipherType = GenericCipherType.AES_CBC_NoPadding;

    public ChannelType getChannelType() {
        return channelType;
    }

    public GenericCipherType getCipherType() {
        return genericCipherType;
    }

    private static volatile GlobalConfig instance = null;

    private GlobalConfig() {}

    public static GlobalConfig getInstance() {
        if (instance == null) {
            synchronized(GlobalConfig.class) {
                if (instance == null) {
                    instance = new GlobalConfig();
                }
            }
        }
        return instance;
    }
}
