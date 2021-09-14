package net.smileycorp.atlas.api.network;

import java.util.function.BiConsumer;

import net.minecraft.network.FriendlyByteBuf;

public class SimpleMessageEncoder<T extends SimpleAbstractMessage> implements BiConsumer<T, FriendlyByteBuf> {

	@Override
	public void accept(T t, FriendlyByteBuf buf) {
		t.write(buf);
	}

}
