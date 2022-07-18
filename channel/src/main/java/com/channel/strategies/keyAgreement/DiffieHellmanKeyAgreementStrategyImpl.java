package com.channel.strategies.keyAgreement;

import com.channel.Channel;
import com.channel.ChannelContext;
import com.channel.ChannelType;
import com.channel.strategies.keyAgreement.diffieHellman.DHKeyAgreement;
import com.channel.strategies.keyAgreement.diffieHellman.DHKeyAgreementInitiator;
import com.channel.strategies.keyAgreement.diffieHellman.DHKeyAgreementReceiver;
import com.commons.enums.EndPoint;
import com.commons.generics.Triplet;

public class DiffieHellmanKeyAgreementStrategyImpl implements KeyAgreementStrategy {

    byte[] macKey, cipherKey, ivKey;

    @Override
    public void agreeOnKeys(ChannelContext channelCtx) throws Exception {

        ChannelContext subChannelCtx = ChannelContext.builder()
                .socket(channelCtx.socket())
                .channelType(ChannelType.UNSECURED)
                .endPoint(EndPoint.SERVER)
                .build();

        Channel unsecuredChannel = Channel.builder()
                .channelContext(subChannelCtx)
                .build();

        DHKeyAgreement diffieHellman;
        if(channelCtx.endPoint().equals(EndPoint.SERVER)) {
            diffieHellman = new DHKeyAgreementReceiver(unsecuredChannel);
        }
        else {
            diffieHellman = new DHKeyAgreementInitiator(unsecuredChannel);
        }

        Triplet<byte[], byte[], byte[]> macKeyAndIv = diffieHellman.exchangeAndDeriveKeys(
                channelCtx.publicCertificate(), channelCtx.signerCertificate(), channelCtx.privateKey()
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
