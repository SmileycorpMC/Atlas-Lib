package net.smileycorp.atlas.api.network;

import java.io.IOException;
import java.util.function.BiConsumer;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class SimpleMessageEncoder<T extends IPacket<INetHandler>> implements BiConsumer<T, PacketBuffer> {

	@Override
	public void accept(T t, PacketBuffer buf) {
		try {
			t.writePacketData(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
