package net.smileycorp.atlas.api.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkUtils {
    
    public static <T extends NetworkMessage> void register(PayloadRegistrar channel, CustomPacketPayload.Type<T> type, Class<T> clazz) {
        channel.playBidirectional(type, new SimpleStreamCodec<>(clazz), T::process);
    }
    
    public static <T extends GenericMessage> void register(PayloadRegistrar channel, CustomPacketPayload.Type<T> type, Class<T> clazz, IPayloadHandler<T> handler) {
        channel.playBidirectional(type, new GenericStreamCodec<>(clazz, type), handler);
    }
    
}
