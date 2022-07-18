package com.channel.strategies.keyAgreement;

import com.channel.ChannelContext;

public interface KeyAgreementStrategy {
    void agreeOnKeys(ChannelContext channelCtx) throws Exception;
    byte[] getMacKey();
    byte[] getCipherKey();
    byte[] getIvKey();
}
