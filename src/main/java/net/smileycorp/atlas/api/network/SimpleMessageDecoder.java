package net.smileycorp.atlas.api.network;

import java.util.function.Function;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class SimpleMessageDecoder<T extends IPacket<INetHandler>> implements Function<PacketBuffer, T> {

	protected final Class<T> clazz;

	public SimpleMessageDecoder(Class<T> clazz) {
		this.clazz=clazz;
	}

	@Override
	public T apply(PacketBuffer buf) {
		try {
			T message = clazz.newInstance();
			message.readPacketData(buf);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		};
		return null;
	}

}
