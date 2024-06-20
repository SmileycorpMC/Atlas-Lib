package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class AbstractStreamCodec<T extends NetworkMessage> implements StreamCodec<FriendlyByteBuf, T> {
    
    protected final Class<T> clazz;
    
    public AbstractStreamCodec(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public T decode(FriendlyByteBuf buf) {
        try {
            T message = clazz.newInstance();
            message.read(buf);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void encode(FriendlyByteBuf buf, T message) {
        message.write(buf);
    }
    
}
