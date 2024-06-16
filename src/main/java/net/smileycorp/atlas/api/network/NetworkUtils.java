package net.smileycorp.atlas.api.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

public class NetworkUtils {

	public static SimpleChannel createChannel(ResourceLocation loc) {
		return NetworkRegistry.getInstance().(loc, ()-> "1", "1"::equals, "1"::equals);
	}

	public static <T extends NetworkMessage> void registerMessage(SimpleChannel channel, int id, Class<T> clazz) {
		channel.registerMessage(id, clazz, new SimpleMessageEncoder<T>(), new SimpleMessageDecoder<T>(clazz), (packet, ctx)->packet.process(ctx.get()));
	}

	public static <T extends NetworkMessage> void registerMessage(SimpleChannel channel, int id, Class<T> clazz, BiConsumer<T, Supplier<NetworkEvent.Context>> function) {
		channel.registerMessage(id, clazz, new SimpleMessageEncoder<T>(), new SimpleMessageDecoder<T>(clazz), function);
	}

}
