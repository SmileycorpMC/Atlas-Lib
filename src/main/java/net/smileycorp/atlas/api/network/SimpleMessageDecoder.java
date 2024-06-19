package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class SimpleMessageDecoder<T extends NetworkMessage> implements Function<FriendlyByteBuf, T> {

	protected final Class<T> clazz;

	public SimpleMessageDecoder(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public T apply(FriendlyByteBuf buf) {
		try {
			T message = clazz.newInstance();
			message.read(buf);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
