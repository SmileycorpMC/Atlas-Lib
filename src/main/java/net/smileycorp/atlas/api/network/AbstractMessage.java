package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.NetworkEvent;

public abstract class AbstractMessage implements Packet<PacketListener> {
	
	public abstract void read(FriendlyByteBuf buf);

	public abstract void process(NetworkEvent.Context ctx);
	
}
