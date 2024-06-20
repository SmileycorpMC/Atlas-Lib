package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.smileycorp.atlas.api.util.TextUtils;


public class GenericTranslatableMessage extends GenericMessage {

	public GenericTranslatableMessage(Type<? extends GenericTranslatableMessage> type) {
		super(type);
	}

	private String key, fallback;
	private Object[] args = {};

	public GenericTranslatableMessage(TranslatableContents contents, Type<? extends GenericTranslatableMessage> type) {
		super(type);
		key = contents.getKey();
		fallback = contents.getFallback();
		args = contents.getArgs();
	}

	public GenericTranslatableMessage(Type<? extends GenericTranslatableMessage> type, String key, String fallback, Object...args) {
		super(type);
		this.key = key;
		this.fallback = fallback;
		this.args = args;
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
	public void process(IPayloadContext ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}
	
}
