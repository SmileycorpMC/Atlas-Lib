package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public abstract class SimpleAbstractMessage implements Packet<PacketListener> {
	
	public abstract void read(FriendlyByteBuf buf);
	
}
