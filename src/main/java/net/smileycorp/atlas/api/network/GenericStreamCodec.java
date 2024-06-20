package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class GenericStreamCodec<T extends GenericMessage> extends SimpleStreamCodec<T> {
    
    private final CustomPacketPayload.Type<T> type;
    
    public GenericStreamCodec(Class<T> clazz, CustomPacketPayload.Type<T> type) {
        super(clazz);
        this.type = type;
    }
    
    @Override
    public T decode(FriendlyByteBuf buf) {
        try {
            T message = clazz.getConstructor(CustomPacketPayload.Type.class).newInstance(type);
            message.read(buf);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
