package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;

public class SimpleMessageEncoder<T extends NetworkMessage> implements BiConsumer<T, FriendlyByteBuf> {

	@Override
	public void accept(T t, FriendlyByteBuf buf) {
		t.write(buf);
	}

}
