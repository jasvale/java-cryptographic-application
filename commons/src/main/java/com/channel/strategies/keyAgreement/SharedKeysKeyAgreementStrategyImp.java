package com.channel.strategies.keyAgreement;

import com.channel.ChannelContext;
import com.commons.generics.Triplet;
import com.commons.utilities.CipherUtils;
import com.commons.utilities.Utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SharedKeysKeyAgreementStrategyImp implements KeyAgreementStrategy {

    byte[] macKey, cipherKey, ivKey;

    @Override
    public void agreeOnKeys(ChannelContext channelCtx) throws IOException, NoSuchAlgorithmException {
        Triplet<byte[], byte[], byte[]> macKeyAndIv = Utils.generateDerivedKeysFromSecretKey(
                CipherUtils.getSharedKey(channelCtx.genericCipherType())
        );
        macKey = macKeyAndIv.first;
        cipherKey = macKeyAndIv.second;
        ivKey = macKeyAndIv.third;
    }

    @Override
    public byte[] getMacKey() {
        return macKey;
    }

    @Override
    public byte[] getCipherKey() {
        return cipherKey;
    }

    @Override
    public byte[] getIvKey() {
        return ivKey;
    }
}
