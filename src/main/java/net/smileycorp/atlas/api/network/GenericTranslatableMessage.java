package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.network.NetworkEvent;
import net.smileycorp.atlas.api.util.TextUtils;


public class GenericTranslatableMessage extends AbstractMessage {

	public GenericTranslatableMessage() {}

	private String key, fallback;
	private Object[] args = {};

	public GenericTranslatableMessage(TranslatableContents contents) {
		key = contents.getKey();
		fallback = contents.getFallback();
		args = contents.getArgs();
	}

	public GenericTranslatableMessage(String key, String fallback, Object...args) {
		this.key=key;
		this.fallback=fallback;
		this.args=args;
	}

	public MutableComponent getComponent() {
		return TextUtils.translatableComponent(key, fallback, args);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUtf(key);
		buf.writeUtf(fallback);
		buf.writeInt(args.length);
		for (Object arg : args) buf.writeUtf(String.valueOf(arg));
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		key = buf.readUtf();
		fallback = buf.readUtf();
		args = new String[buf.readInt()];
		for (int i = 0; i < args.length; i++) args[i] = buf.readUtf();
	}

	@Override
	public void handle(PacketListener listener) {}

	@Override
	public void process(NetworkEvent.Context ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}

}
