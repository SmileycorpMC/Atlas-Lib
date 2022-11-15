package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;


public class SimpleTranslatableMessage extends SimpleAbstractMessage {

	public SimpleTranslatableMessage() {}

	private String key;
	private String [] args = {};

	public SimpleTranslatableMessage(String key, String...args) {
		this.key=key;
		this.args=args;
	}

	public String getKey() {
		return key;
	}

	public String[] getArgs() {
		return args;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUtf(key);
		buf.writeInt(args.length);
		for (String arg : args) buf.writeUtf(arg);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		key = buf.readUtf();
		args = new String[buf.readInt()];
		for (int i = 0; i < args.length; i++) args[i] = buf.readUtf();
	}

	@Override
	public void handle(PacketListener listener) {}

}
