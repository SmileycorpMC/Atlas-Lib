package net.smileycorp.atlas.api.network;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class SimpleMessageDecoder<T extends IPacket<INetHandler>> implements Function<PacketBuffer, T> {

	@Override
	@SuppressWarnings("unchecked")
	public T apply(PacketBuffer buf) {
		try {
			T message = ((Class<T>)((ParameterizedType)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getRawType()).newInstance();
			message.readPacketData(buf);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		};
		return null;
	}

}
