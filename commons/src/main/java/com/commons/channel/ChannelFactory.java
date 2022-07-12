package com.commons.channel;

import com.commons.enums.EndPoint;
import java.net.Socket;

public class ChannelFactory {

    public static ChannelType channelType = ChannelType.SECURED_DIFFIE_HELLMAN;

    public static IChannel getChannel(
        ChannelContext channelContext
    ) throws Exception {
        switch (channelContext.channelType()) {
            case SECURED_SHARED_KEYS -> {
                return new SecuredChannelSharedKeys(channelContext);
            }
            case SECURED_DIFFIE_HELLMAN -> {
                return new SecuredChannelDiffieHellman(channelContext);
            }
        }
        return null;
    }

    public static IChannel getChannel(
            ChannelType channelType,
            Socket socket,
            EndPoint endPoint
    ) {
        if (channelType == ChannelType.UNSECURED) {
            return new UnsecuredChannel(socket, endPoint);
        }
        return null;
    }
}
