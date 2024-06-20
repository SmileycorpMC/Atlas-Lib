package net.smileycorp.atlas.api.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public abstract class GenericMessage implements NetworkMessage {
    
    private final Type<? extends GenericMessage> type;
    
    public GenericMessage(Type<? extends GenericMessage> type) {
        this.type = type;
    }
    
    @Override
    public final Type<? extends CustomPacketPayload> type() {
        return type;
    }
    
}
