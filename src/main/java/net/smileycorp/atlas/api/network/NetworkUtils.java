package net.smileycorp.atlas.api.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NetworkUtils {

	public static SimpleChannel createChannel(ResourceLocation loc) {
		return NetworkRegistry.newSimpleChannel(loc, ()-> "1", "1"::equals, "1"::equals);
	}

	public static <T extends SimpleAbstractMessage> void registerMessage(SimpleChannel channel, int id, Class<T> clazz, BiConsumer<T, Supplier<NetworkEvent.Context>> function) {
		channel.registerMessage(id, clazz, new SimpleMessageEncoder<T>(), new SimpleMessageDecoder<T>(clazz), function);
	}

}
