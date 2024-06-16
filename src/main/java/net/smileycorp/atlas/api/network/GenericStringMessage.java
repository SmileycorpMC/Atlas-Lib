package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class GenericStringMessage implements NetworkMessage {

	public GenericStringMessage() {}

	private String text;

	public GenericStringMessage(String text) {
		this.text=text;
	}

	public String getText() {
		return text;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUtf(text);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		text = buf.readUtf();
	}

	@Override
	public void process(IPayloadContext ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return null;
	}
	
}
