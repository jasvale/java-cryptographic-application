package com.channel;

import com.channel.strategies.encryption.WithEncryptionStrategyImpl;
import com.channel.strategies.encryption.WithoutEncryptionStrategyImpl;
import com.channel.strategies.keyAgreement.DiffieHellmanKeyAgreementStrategyImpl;
import com.channel.strategies.keyAgreement.SharedKeysKeyAgreementStrategyImp;
import com.channel.strategies.keyAgreement.WithoutKeyAgreementStrategyImpl;
import com.channel.strategies.signing.WithSigningStrategyImpl;
import com.channel.strategies.signing.WithoutSigningStrategyImpl;

import java.io.IOException;

public interface Channel {
    ChannelContext getChannelContext();
    void send(byte[] bytes) throws Exception;
    byte[] receive() throws Exception;
    void close() throws IOException;

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private ChannelContext channelCtx;

        public Builder channelContext(ChannelContext channelCtx) {
            this.channelCtx = channelCtx;
            return this;
        }

        public Channel build() throws Exception {
            if(channelCtx == null)
                throw new Exception("Cannot build() Channel, is not valid, channelCtx is null.");
            if(channelCtx.channelType() == null)
                throw new Exception("Cannot build() Channel, is not valid, channelCtx.channelType() is null.");

            switch (channelCtx.channelType()) {
                case UNSECURED -> {
                    return new BaseChannel(channelCtx,
                            new WithoutKeyAgreementStrategyImpl(),
                            new WithoutEncryptionStrategyImpl(),
                            new WithoutSigningStrategyImpl()
                    );
                }
                case SECURED_SHARED_KEYS -> {
                    return new BaseChannel(channelCtx,
                            new SharedKeysKeyAgreementStrategyImp(),
                            new WithEncryptionStrategyImpl(),
                            new WithSigningStrategyImpl()
                    );
                }
                case SECURED_DIFFIE_HELLMAN -> {
                    return new BaseChannel(channelCtx,
                            new DiffieHellmanKeyAgreementStrategyImpl(),
                            new WithEncryptionStrategyImpl(),
                            new WithSigningStrategyImpl()
                    );
                }
            }
            return null;
        }
    }
}
