package com.channel.strategies.keyAgreement;

import com.channel.ChannelContext;
import com.commons.utilities.Console;

public class WithoutKeyAgreementStrategyImpl implements KeyAgreementStrategy {

    @Override
    public void agreeOnKeys(ChannelContext channelCtx) {
        Console.show("No key agreement strategy is configured.");
    }

    @Override
    public byte[] getMacKey() {
        return null;
    }

    @Override
    public byte[] getCipherKey() {
        return null;
    }

    @Override
    public byte[] getIvKey() {
        return null;
    }
}
