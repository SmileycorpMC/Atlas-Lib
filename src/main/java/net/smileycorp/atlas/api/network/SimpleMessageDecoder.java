package net.smileycorp.atlas.api.network;

import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;

public class SimpleMessageDecoder<T extends SimpleAbstractMessage> implements Function<FriendlyByteBuf, T> {

	protected final Class<T> clazz;

	public SimpleMessageDecoder(Class<T> clazz) {
		this.clazz=clazz;
	}

	@Override
	@SuppressWarnings("deprecation")
	public T apply(FriendlyByteBuf buf) {
		try {
			T message = clazz.newInstance();
			message.read(buf);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		};
		return null;
	}

}
