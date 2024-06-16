package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface NetworkMessage extends CustomPacketPayload {
	
	void read(FriendlyByteBuf buf);
	
	void write(FriendlyByteBuf buf);

	void process(IPayloadContext ctx);
	
}
